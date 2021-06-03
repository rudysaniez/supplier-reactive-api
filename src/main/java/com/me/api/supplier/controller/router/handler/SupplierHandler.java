package com.me.api.supplier.controller.router.handler;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.me.api.supplier.controller.config.PropertiesConfig.PaginationInformation;
import com.me.api.supplier.domain.SupplierEntity;
import com.me.api.supplier.exception.InvalidInputException;
import com.me.api.supplier.mapper.SupplierMapper;
import com.me.api.supplier.model.PageMetadata;
import com.me.api.supplier.model.PagedSupplier;
import com.me.api.supplier.model.Supplier;
import com.me.api.supplier.repository.SupplierRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Using functional endpoints.
 * @author rudysaniez
 */
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
		
		// @formatter:off
		Mono<ServerResponse> notFound = ServerResponse.
				notFound().
				build();
		
		return supplierRepository.
				findBySupplierId(request.pathVariable("supplierId")).
				map(mapper::toModel).
				flatMap(supplier -> ServerResponse.ok().
										contentType(MediaType.APPLICATION_JSON).
										bodyValue(supplier)).
				switchIfEmpty(notFound);
		// @formatter:on
	}
	
	/**
	 * @param request
	 * @return {@link ServerResponse a paged of suppliers}
	 */
	public Mono<ServerResponse> getManySuppliers(ServerRequest request) {
		
		Optional<String> page = request.queryParam("page");
		Optional<String> size = request.queryParam("size");
		
		final Integer fpage;
		final Integer fsize;
		
		fpage = (page.isEmpty() || Integer.valueOf(page.get()) < 0) ? pagination.getDefaultPage() : Integer.valueOf(page.get());
		fsize = (size.isEmpty() || Integer.valueOf(page.get()) < 0) ? pagination.getDefaultSize() : Integer.valueOf(size.get());
		
		if(request.queryParam("name").isPresent()) {
 
			String name = request.queryParam("name").get();
			
			log.info(" > Search many suppliers by the name={}.", name);
			
			// @formatter:off
			return supplierRepository.countByNameContaining(name).
					transform(m -> m.flatMap(count -> supplierRepository.findByNameContaining(name, PageRequest.of(fpage, fsize, Sort.by(Direction.ASC, "supplierId"))).
								map(mapper::toModel).
								collectList().
								map(list -> PagedSupplier.builder().content(list).
									page(PageMetadata.builder().
											number(Integer.toUnsignedLong(fpage)).
											size(Integer.toUnsignedLong(fsize)).
											totalElements(count).
											totalPages(count < fsize ? 1L : count % fsize == 0 ? count / fsize : (count / fsize) + 1) .build()).build())
								)
					).
					flatMap(pagedSuppliers -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(pagedSuppliers)).
					log();
			// @formatter:on
		}
		else {
			
			log.info(" > Search many suppliers without parameters.");
			
			// @formatter:off
			return supplierRepository.count().
					transform(m -> m.flatMap(count -> supplierRepository.findBySupplierIdNotNull(PageRequest.of(fpage, fsize, Sort.by(Direction.ASC, "supplierId"))).
							map(mapper::toModel).collectList().
							map(list -> PagedSupplier.builder().content(list).
									page(PageMetadata.builder().
											number(Integer.toUnsignedLong(fpage)).
											size(Integer.toUnsignedLong(fsize)).
											totalElements(count).
											totalPages(count < fsize ? 1L : count % fsize == 0 ? count / fsize : (count / fsize) + 1).build()).build())
							)
					).
					flatMap(pagedSuppliers -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(pagedSuppliers)).
					log();
			// @formatter:on
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
			switchIfEmpty(Mono.error(new InvalidInputException(" > The supplier is incorrect."))).
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
				flatMap(supplierEntity -> supplierRepository.delete(supplierEntity).thenReturn(supplierEntity)).
				flatMap(supplierEntity -> ServerResponse.ok().build()).
				switchIfEmpty(ServerResponse.notFound().build());
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
}
