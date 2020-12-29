package com.me.api.supplier.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.me.api.supplier.bo.SupplierEntity;

import reactor.core.publisher.Mono;

@Transactional
public interface SupplierRepository extends ReactiveCrudRepository<SupplierEntity, Integer> {

	/**
	 * @param supplierId
	 * @return {@link SupplierEntity}
	 */
	@Transactional(readOnly = true)
	public Mono<SupplierEntity> findBySupplierId(String supplierId);
}
