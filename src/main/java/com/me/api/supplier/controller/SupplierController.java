package com.me.api.supplier.controller;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.me.api.supplier.Application.PaginationInformation;
import com.me.api.supplier.exception.InvalidInputException;
import com.me.api.supplier.exception.NotFoundException;
import com.me.api.supplier.mapper.SupplierMapper;
import com.me.api.supplier.model.PageMetadata;
import com.me.api.supplier.model.PagedSupplier;
import com.me.api.supplier.model.Supplier;
import com.me.api.supplier.model.SupplierApi;
import com.me.api.supplier.repository.SupplierRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
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
		
		log.info(" > Search a supplier by supplierId={}", supplierId);
		
		return supplierRepository.findBySupplierId(supplierId).
			switchIfEmpty(Mono.error(new NotFoundException(String.format("The supplier with supplierId=%s doesn't not exists.", supplierId)))).
			log().
			map(mapper::toModel).
			map(s -> ResponseEntity.ok(s));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Mono<ResponseEntity<PagedSupplier>> findByParam(String name, Integer page, Integer size) {
		
		if(page == null || page < 0) page = paginationInformation.getDefaultPage();
		if(size == null || size < 1) size = paginationInformation.getDefaultSize();
		
		final Integer pageNumber = page;
		final Integer pageSize = size;
		
		if(StringUtils.isNotEmpty(name)) {
			
			return supplierRepository.countByNameStartingWith(name).
				log().
				transform(m -> m.flatMap(count -> supplierRepository.findByNameStartingWith(name, PageRequest.of(pageNumber, pageSize, Sort.by(Direction.ASC, "supplierId"))).
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
		}
		else {
			
			return supplierRepository.count().
				log().
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
		}
	}
}
