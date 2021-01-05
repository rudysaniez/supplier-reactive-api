package com.me.api.supplier.test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.me.api.supplier.service.id.SupplierIdService;

public class SupplierIdTest {

	private static final String BU = "FR";
	private static final String STORE_ID_FORMAT = "000";
	private static final Integer STORE_ID_MAX = 999;
	private static final String SUPPLIER_ID_FORMAT = "00000000";
	private static final Integer SUPPLIER_ID_MAX = 99999999;
	
	@Autowired 
	private static SupplierIdService idService;
	
	static {
		idService = new SupplierIdService(BU, STORE_ID_FORMAT, STORE_ID_MAX, SUPPLIER_ID_FORMAT, SUPPLIER_ID_MAX);
	}
	
	@Test
	public void supplierId() {
		
		assertEquals(idService.getBu(), "FR");
		assertTrue(idService.getStoreId().length() == STORE_ID_FORMAT.length());
		assertTrue(idService.getSupplierId().length() == SUPPLIER_ID_FORMAT.length());
		
		String id = idService.getId();
		assertTrue(id.contains(idService.getBu()));
	}
}
