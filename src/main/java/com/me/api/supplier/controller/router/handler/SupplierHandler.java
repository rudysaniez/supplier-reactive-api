package com.me.api.supplier.controller.router.handler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;

import com.me.api.supplier.controller.config.PropertiesConfig.PaginationInformation;
import com.me.api.supplier.domain.SupplierEntity;
import com.me.api.supplier.mapper.SupplierMapper;
import com.me.api.supplier.model.HttpErrorInfo;
import com.me.api.supplier.model.PageMetadata;
import com.me.api.supplier.model.PagedSupplier;
import com.me.api.supplier.model.Supplier;
import com.me.api.supplier.repository.SupplierRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class SupplierHandler {

	private SupplierRepository supplierRepository;
	private PaginationInformation pagination;
	private SupplierMapper mapper = Mappers.getMapper(SupplierMapper.class);
	
	public SupplierHandler(SupplierRepository supplierRepository, PaginationInformation pagination) {
		this.supplierRepository = supplierRepository;
		this.pagination = pagination;
	}
	
	/**
	 * @param request
	 * @return {@link ServerResponse the supplier}
	 */
	public Mono<ServerResponse> getBySupplierId(ServerRequest request) {
		
		log.info(" > Search a supplier by supplierId={}.", request.pathVariable("supplierId"));
		
		final String supplierId = request.pathVariable("supplierId");
		
		// @formatter:off
		return supplierRepository.
				findBySupplierId(supplierId).
				map(mapper::toModel).
				flatMap(supplier -> ServerResponse.ok().
										contentType(MediaType.APPLICATION_JSON).
										bodyValue(supplier)
				).
				switchIfEmpty(handlerAdvice(HttpStatus.NOT_FOUND, 
						String.format("The supplier with identifier %s does not exist.", supplierId), 
						request.exchange())
				);
		// @formatter:on
	}
	
	/**
	 * @param request
	 * @return {@link ServerResponse a paged of suppliers}
	 */
	public Mono<ServerResponse> getManySuppliers(ServerRequest request) {
		
		try {
			
			final Integer pageNumber = request.queryParam("page").isPresent() ? Integer.valueOf(request.queryParam("page").get()) : pagination.getDefaultPage();
			final Integer pageSize = request.queryParam("size").isPresent() ? Integer.valueOf(request.queryParam("size").get()) : pagination.getDefaultSize();
			
			if(request.queryParam("name").isPresent()) {
	 
				String name = request.queryParam("name").get();
				
				log.info(" > Search many suppliers by the name={}.", name);
				
				// @formatter:off
				return supplierRepository.countByNameContaining(name).
						flatMap(count -> supplierRepository.findByNameContaining(name, PageRequest.of(pageNumber, pageSize, Sort.by(Direction.ASC, "supplierId"))).
							map(mapper::toModel).
							collectList().
							map(list -> toPagedSupplier(count, pageNumber, pageSize, list))
						).
						flatMap(pagedSuppliers -> ServerResponse.ok().
								contentType(MediaType.APPLICATION_JSON).
								bodyValue(pagedSuppliers)).
						log();
				// @formatter:on
			}
			else {
				
				log.info(" > Search many suppliers without parameters.");
				
				// @formatter:off
				return supplierRepository.count().
						flatMap(count -> supplierRepository.findBySupplierIdNotNull(PageRequest.of(pageNumber, pageSize, Sort.by(Direction.ASC, "supplierId"))).
							map(mapper::toModel).
							collectList().
							map(list -> toPagedSupplier(count, pageNumber, pageSize, list))
						).
						flatMap(pagedSuppliers -> ServerResponse.ok().
								contentType(MediaType.APPLICATION_JSON).
								bodyValue(pagedSuppliers)
						).
						log();
				// @formatter:on
			}
		}
		catch(NumberFormatException e) {
			return handlerAdvice(HttpStatus.BAD_REQUEST, "The query parameters named page and size must be a number.", request.exchange());
		}
	}
	
	/**
	 * @param request
	 * @return {@link ServerResponse the supplier updated or created}
	 */
	public Mono<ServerResponse> saveOfUpdate(ServerRequest request) {
		
		log.info(" > Save or update a supplier.");
		
		// @formatter:off
		return request.
			bodyToMono(Supplier.class).
			filter(s -> StringUtils.isNotBlank(s.getSupplierId()) && StringUtils.isNotBlank(s.getName()) && 
					StringUtils.isNotBlank(s.getFiscalId())).
			map(mapper::toEntity).
			map(this::initCreationDate).
			flatMap(entityBeforeSearch -> supplierRepository.findBySupplierId(entityBeforeSearch.getSupplierId()).
					map(entityAfterSearch -> merge(entityAfterSearch, entityBeforeSearch)).
					flatMap(supplierRepository::save).
					map(mapper::toModel).
					flatMap(entitySaved -> ServerResponse.ok().bodyValue(entitySaved)).
					switchIfEmpty(supplierRepository.save(entityBeforeSearch).
							map(mapper::toModel).
							flatMap(entityCreated -> ServerResponse.ok().bodyValue(entityCreated)))
			).
			switchIfEmpty(handlerAdvice(HttpStatus.UNPROCESSABLE_ENTITY, 
					"The supplier input is incorrect. The fields supplierId, name and fiscalId are required.", 
					request.exchange())
			).
			onErrorResume(t -> handlerAdvice(HttpStatus.UNPROCESSABLE_ENTITY, 
					"The supplier persistence failed, check your input supplier.", t, request.exchange())
			).
			log();
		// @formatter:on
	}
	
	/**
	 * @param request
	 * @return {@link ServerResponse no response body}
	 */
	public Mono<ServerResponse> delete(ServerRequest request) {
		
		String supplierId = request.pathVariable("supplierId");
		
		log.info(" > Delete this supplier {}.", supplierId);
		
		// @formatter:off
		return supplierRepository.findBySupplierId(supplierId).
				flatMap(supplierEntity -> supplierRepository.delete(supplierEntity).
						thenReturn(supplierEntity)
				).
				flatMap(supplierEntity -> ServerResponse.ok().build()).
				switchIfEmpty(handlerAdvice(HttpStatus.NOT_FOUND, 
						String.format("The supplier with identifier %s does not exist", supplierId), 
						request.exchange())
				);
		// @formatter:on
	}
	
	/**
	 * @param target
	 * @param from
	 * @return {@link SupplierEntity}
	 */
	private SupplierEntity merge(SupplierEntity target, SupplierEntity from) {
		
		target.setUpdateDate(LocalDateTime.now());
		target.setFiscalCountryCode(from.getFiscalCountryCode());
		target.setFiscalId(from.getFiscalId());
		target.setName(from.getName());
		return target;
	}
	
	/**
	 * @param in
	 * @return {@link SupplierEntity}
	 */
	private SupplierEntity initCreationDate(SupplierEntity in) {
		
		if(Objects.isNull(in.getCreationDate()))
			in.setCreationDate(LocalDateTime.now());
		
		return in;
	}
	
	/**
	 * @param count
	 * @param pageNumber
	 * @param pageSize
	 * @param products
	 * @return {@link PagedSupplier page of suppliers}
	 */
	private PagedSupplier toPagedSupplier(Long count, Integer pageNumber, Integer pageSize, List<Supplier> products) {
		
		return PagedSupplier.builder().
				content(products).
				page(PageMetadata.builder().
						number(Integer.toUnsignedLong(pageNumber)).
						size(Integer.toUnsignedLong(pageSize)).
						totalElements(count).
						totalPages(count < pageSize ? 1L : count % pageSize == 0 ? count / pageSize : (count / pageSize) + 1).build()).build();
	}
	
	/**
	 * @param status
	 * @param message
	 * @param exchange
	 * @return
	 */
	private Mono<ServerResponse> handlerAdvice(HttpStatus status, String message, ServerWebExchange exchange) {
		
		// @formatter:off
		return ServerResponse.status(status).bodyValue(HttpErrorInfo.builder().
													message(message).
													httpStatus(status.value()).
													path(exchange.getRequest().getPath().pathWithinApplication().value()).
													build());
		// @formatter:on
	}
	
	/**
	 * @param status
	 * @param message
	 * @param throwable
	 * @param exchange
	 * @return
	 */
	private Mono<ServerResponse> handlerAdvice(HttpStatus status, String message, Throwable throwable, ServerWebExchange exchange) {
		
		log.error(message, throwable);
		
		// @formatter:off
		return ServerResponse.status(status).bodyValue(HttpErrorInfo.builder().
													message(message.concat(" The exception message is ").concat(throwable.getMessage())).
													httpStatus(status.value()).
													path(exchange.getRequest().getPath().pathWithinApplication().value()).
													build());
		// @formatter:on
	}
}
