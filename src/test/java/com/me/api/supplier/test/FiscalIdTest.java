package com.me.api.supplier.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.me.api.supplier.service.id.FiscalIdService;

public class FiscalIdTest {

	private static final String FISCAL_ID_FORMAT = "00000000";
	private static final Integer FISCAL_ID_MAX = 999999;
	
	private static final FiscalIdService fiscalIdService;
	
	static {
		fiscalIdService = new FiscalIdService(FISCAL_ID_FORMAT, FISCAL_ID_MAX);
	}
	
	@Test
	public void getFiscalId() {
		
		String fiscalId = fiscalIdService.getId();
		assertTrue(fiscalId.contains(FiscalIdService.DOMAIN));
	}
}
