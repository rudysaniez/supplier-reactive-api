package com.me.api.supplier.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.me.api.supplier.domain.AddressEntity;

public interface AddressRepository extends R2dbcRepository<AddressEntity, Integer> {

}
