package com.me.api.supplier.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.me.api.supplier.bo.SupplierEntity;
import com.me.api.supplier.repository.SupplierRepository;
import com.me.api.supplier.service.console.AsciiArtService;
import com.me.api.supplier.service.id.FiscalIdService;
import com.me.api.supplier.service.id.SupplierIdService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
@RunWith(SpringRunner.class)
public class SupplierEntityTest {

	@Autowired private SupplierRepository supplierRepository;
	@Autowired private AsciiArtService asciiArtService;
	@Autowired private SupplierIdService supplierIdService;
	@Autowired private FiscalIdService fiscalIdService;
	
	@Before
	public void setup() {
		
		asciiArtService.display("SETUP");
		
		supplierRepository.deleteAll().block();
		
		SupplierEntity supplierCreated = supplierRepository.save(new SupplierEntity(null, supplierIdService.getId(), 
				"DEXTER", "001", fiscalIdService.getId(), 
				SupplierEntity.Type.SUPPLIER.name())).block();
		
		log.info(" > Supplier has been created : {}", supplierCreated.toString());
	}
	
	@Test
	public void findAll() {
		
		asciiArtService.display("FIND ALL");
		
		List<SupplierEntity> suppliers = supplierRepository.findAll().collectList().block();
		assertThat(suppliers).isNotEmpty();
	}
}
