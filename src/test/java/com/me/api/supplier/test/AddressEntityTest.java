package com.me.api.supplier.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.me.api.supplier.domain.AddressEntity;
import com.me.api.supplier.repository.AddressRepository;
import com.me.api.supplier.service.console.AsciiArtService;
import com.me.api.supplier.service.id.SupplierIdService;

@ExtendWith(SpringExtension.class)
@DataR2dbcTest
public class AddressEntityTest {

	@Autowired AddressRepository addressRepository;
	@Autowired SupplierIdService supplierIdService;
	@Autowired AsciiArtService asciiArtService;
	
	@BeforeEach
	public void setup() {
		
		asciiArtService.display("SETUP");
		
		addressRepository.deleteAll().block();
		
		addressRepository.save(new AddressEntity(null, "222 rue du bois", "La Madeleine", "France", "59110", 
				supplierIdService.getId(), LocalDateTime.now(), LocalDateTime.now())).
				log().
				block();
	}
	
	@Test
	public void findAll() {
		
		List<AddressEntity> address = addressRepository.
				findAll().
				collectList().
				block();
		
		assertThat(address).isNotEmpty();
	}
}
