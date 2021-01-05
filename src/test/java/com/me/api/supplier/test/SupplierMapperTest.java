package com.me.api.supplier.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;
import org.mapstruct.factory.Mappers;

import com.me.api.supplier.bo.SupplierEntity;
import com.me.api.supplier.mapper.SupplierMapper;
import com.me.api.supplier.model.Supplier;

public class SupplierMapperTest {

	private SupplierMapper mapper = Mappers.getMapper(SupplierMapper.class);
	
	@Test
	public void toEntity() {
		
		Supplier supplier = Supplier.builder().amountOfCapital(10000L).creationDate(LocalDateTime.now()).
				fiscalCountryCode("001").fiscalId("FIS_0001").legalId("LEG_0001").name("DEXTER").
				otherName("DEXTER_POWER").secondSupplierOtherName("DEXTER_OTHER").shortName("DEXT").
				supplierId("SUP_FR_001@0987654").supplierType(SupplierEntity.Type.SUPPLIER.name()).
				updateDate(LocalDateTime.now()).build();
		
		SupplierEntity entity = mapper.toEntity(supplier);
		
		assertEquals(entity.getSupplierId(), supplier.getSupplierId());
		assertEquals(entity.getFiscalCountryCode(), supplier.getFiscalCountryCode());
		assertEquals(entity.getLegalId(), supplier.getLegalId());
		assertEquals(entity.getFiscalId(), supplier.getFiscalId());
		assertEquals(entity.getName(), supplier.getName());
		assertEquals(entity.getSupplierType(), supplier.getSupplierType());
	}
}
