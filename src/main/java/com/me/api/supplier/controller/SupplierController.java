package com.me.api.supplier.controller;

import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.me.api.supplier.controller.config.PropertiesConfig.PaginationInformation;
import com.me.api.supplier.domain.SupplierEntity;
import com.me.api.supplier.exception.InvalidInputException;
import com.me.api.supplier.exception.NotFoundException;
import com.me.api.supplier.mapper.SupplierMapper;
import com.me.api.supplier.model.PageMetadata;
import com.me.api.supplier.model.PagedSupplier;
import com.me.api.supplier.model.Supplier;
import com.me.api.supplier.model.SupplierApi;
import com.me.api.supplier.repository.SupplierRepository;

import reactor.core.publisher.Mono;

/**
 * Using annotated endpoints.
 * @author rudysaniez
 */
@RestController
public class SupplierController implements SupplierApi {

	private SupplierRepository supplierRepository;
	private PaginationInformation paginationInformation;
	private SupplierMapper mapper = Mappers.getMapper(SupplierMapper.class);
	
	@Autowired
	public SupplierController(SupplierRepository supplierRepository, PaginationInformation paginationInformation) {
		
		this.supplierRepository = supplierRepository;
		this.paginationInformation = paginationInformation;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Mono<ResponseEntity<Supplier>> getBySupplierId(String supplierId, ServerWebExchange exchange) {
		
		if(StringUtils.isEmpty(supplierId)) throw new InvalidInputException("The supplierId must not be empty.");
		
		// @formatter:off
		return supplierRepository.findBySupplierId(supplierId).
			switchIfEmpty(Mono.error(new NotFoundException(String.format("The supplier with supplierId=%s doesn't not exists.", supplierId)))).
			map(mapper::toModel).
			map(s -> ResponseEntity.ok(s)).
			log();
		// @formatter:on
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Mono<ResponseEntity<PagedSupplier>> findByParam(String name, Integer page, Integer size, ServerWebExchange exchange) {
		
		if(page == null || page < 0) page = paginationInformation.getDefaultPage();
		if(size == null || size < 1) size = paginationInformation.getDefaultSize();
		
		final Integer pageNumber = page;
		final Integer pageSize = size;
		
		if(StringUtils.isNotBlank(name)) {
			
			// @formatter:off
			return supplierRepository.countByNameContaining(name).
				transform(m -> m.flatMap(count -> supplierRepository.findByNameContaining(name, PageRequest.of(pageNumber, pageSize, Sort.by(Direction.ASC, "supplierId"))).
							map(mapper::toModel).collectList().
							map(list -> PagedSupplier.builder().content(list).
								page(PageMetadata.builder().
										number(Integer.toUnsignedLong(pageNumber)).
										size(Integer.toUnsignedLong(pageSize)).
										totalElements(count).
										totalPages(count < pageSize ? 1L : count % pageSize == 0 ? count / pageSize : (count / pageSize) + 1) .build()).build())
							)
				).
				map(pagedSupplier -> ResponseEntity.ok(pagedSupplier)).
				log();
			// @formatter:on
		}
		else {
			
			// @formatter:off
			return supplierRepository.count().
				transform(m -> m.flatMap(count -> supplierRepository.findBySupplierIdNotNull(PageRequest.of(pageNumber, pageSize, Sort.by(Direction.ASC, "supplierId"))).
						map(mapper::toModel).collectList().
						map(list -> PagedSupplier.builder().content(list).
								page(PageMetadata.builder().
										number(Integer.toUnsignedLong(pageNumber)).
										size(Integer.toUnsignedLong(pageSize)).
										totalElements(count).
										totalPages(count < pageSize ? 1L : count % pageSize == 0 ? count / pageSize : (count / pageSize) + 1).build()).build())
						)
				).
				map(pagedSupplier -> ResponseEntity.ok(pagedSupplier)).
				log();
			// @formatter:on
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Mono<ResponseEntity<Supplier>> saveOrUpdate(Mono<Supplier> supplier, ServerWebExchange exchange) {
		
		// @formatter:off
		return supplier.filter(s -> StringUtils.isNotBlank(s.getSupplierId()) && StringUtils.isNotBlank(s.getName()) && 
					StringUtils.isNotBlank(s.getFiscalId())).
				switchIfEmpty(Mono.error(new InvalidInputException(" > The supplier is incorrect."))).
				map(mapper::toEntity).
				flatMap(supplierBeforeSearch -> supplierRepository.findBySupplierId(supplierBeforeSearch.getSupplierId()).
						map(supplierFound -> merge(supplierBeforeSearch, supplierFound)).
						flatMap(supplierRepository::save).
							map(mapper::toModel).
							map(supplierUpdated -> ResponseEntity.ok(supplierUpdated)).
						
						switchIfEmpty(supplierRepository.save(supplierBeforeSearch).
							map(mapper::toModel).
							map(supplierCreated -> ResponseEntity.status(HttpStatus.CREATED).body(supplierCreated)))
				).
				log();
		// @formatter:on
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Mono<ResponseEntity<Void>> delete(String supplierId, ServerWebExchange exchange) {
		
		if(StringUtils.isEmpty(supplierId)) throw new InvalidInputException("The supplierId doesn't not empty.");
		
		// @formatter:off
		return supplierRepository.
				deleteBySupplierId(supplierId).
				map(v -> ResponseEntity.ok(v));
		// @formatter:on
	}
	
	/**
	 * @param supplierBeforeSearch
	 * @param supplierFound
	 * @return {@link SupplierEntity}
	 */
	private SupplierEntity merge(SupplierEntity supplierBeforeSearch, SupplierEntity supplierFound) {
		
		supplierFound.setFiscalCountryCode(supplierBeforeSearch.getFiscalCountryCode());
		supplierFound.setFiscalId(supplierBeforeSearch.getFiscalId());
		supplierFound.setName(supplierBeforeSearch.getName());
		supplierFound.setUpdateDate(LocalDateTime.now());
		
		return supplierFound;
	}
}
