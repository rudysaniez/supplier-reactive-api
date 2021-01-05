package com.me.api.supplier.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.me.api.supplier.bo.SupplierEntity;
import com.me.api.supplier.model.Supplier;

@Mapper
public interface SupplierMapper {

	/**
	 * @param supplierEntity
	 * @return {@link Supplier}
	 */
	public Supplier toModel(SupplierEntity supplierEntity);
	
	/**
	 * @param supplier
	 * @return {@link SupplierEntity}
	 */
	@Mappings(value = {@Mapping(target = "id", ignore = true), @Mapping(target = "withId", ignore = true)})
	public SupplierEntity toEntity(Supplier supplier);
}
