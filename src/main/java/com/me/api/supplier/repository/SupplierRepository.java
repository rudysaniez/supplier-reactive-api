package com.me.api.supplier.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.transaction.annotation.Transactional;

import com.me.api.supplier.domain.SupplierEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Transactional
public interface SupplierRepository extends R2dbcRepository<SupplierEntity, Integer> {

	/**
	 * @param supplierId
	 * @return {@link SupplierEntity}
	 */
	@Transactional(readOnly = true)
	public Mono<SupplierEntity> findBySupplierId(String supplierId);
	
	/**
	 * @param name
	 * @param page
	 * @return {@link SupplierEntity}
	 */
	@Transactional(readOnly = true)
	public Flux<SupplierEntity> findByNameContaining(String name, Pageable page);
	
	/**
	 * @param name
	 * @return {@link Long}
	 */
	@Transactional(readOnly = true)
	public Mono<Long> countByNameContaining(String name);
	
	/**
	 * @param page
	 * @return {@link SupplierEntity}
	 */
	@Transactional(readOnly = true)
	public Flux<SupplierEntity> findBySupplierIdNotNull(Pageable page);
	
	/**
	 * @param supplierId
	 * @return {@link Void}
	 */
	public Mono<Void> deleteBySupplierId(String supplierId);
}
