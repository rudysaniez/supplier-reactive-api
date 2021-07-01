package com.me.api.supplier.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.me.api.supplier.domain.SupplierEntity;
import com.me.api.supplier.repository.SupplierRepository;
import com.me.api.supplier.service.console.AsciiArtService;
import com.me.api.supplier.service.id.FiscalIdService;
import com.me.api.supplier.service.id.SupplierIdService;

@ExtendWith(SpringExtension.class)
@DataR2dbcTest
public class SupplierEntityTest {

	@Autowired private SupplierRepository supplierRepository;
	@Autowired private AsciiArtService asciiArtService;
	@Autowired private SupplierIdService supplierIdService;
	@Autowired private FiscalIdService fiscalIdService;
	@MockBean ReactiveJwtDecoder darthNimbusReactiveJwtDecoder;
	
	@BeforeEach
	public void setup() {
		
		asciiArtService.display("SETUP");
		
		supplierRepository.deleteAll().block();
		
		supplierRepository.save(new SupplierEntity(null, supplierIdService.getId(), 
				"DEXTER", "001", fiscalIdService.getId(), 
				LocalDateTime.now(), LocalDateTime.now(),
				new Integer[] {1, 2, 6, 9})).
				log().
				block();
	}
	
	@Test
	public void findAll() {
		
		asciiArtService.display("FIND ALL");
		
		List<SupplierEntity> suppliers = supplierRepository.findAll().collectList().log().block();
		assertThat(suppliers).isNotEmpty();
	}
}
