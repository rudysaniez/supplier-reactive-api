package com.me.api.supplier.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.BodyContentSpec;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.me.api.supplier.Api;
import com.me.api.supplier.bo.SupplierEntity;
import com.me.api.supplier.mapper.SupplierMapper;
import com.me.api.supplier.model.Supplier;
import com.me.api.supplier.repository.SupplierRepository;
import com.me.api.supplier.service.console.AsciiArtService;
import com.me.api.supplier.service.id.FiscalIdService;
import com.me.api.supplier.service.id.SupplierIdService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class SupplierApiTest {

	@Autowired private WebTestClient client;
	@Autowired private SupplierRepository supplierRepository;
	@Autowired private AsciiArtService asciiArtService;
	@Autowired private SupplierIdService supplierIdService;
	@Autowired private FiscalIdService fiscalIdService;
	@LocalServerPort private int port;
	
	private SupplierMapper mapper = Mappers.getMapper(SupplierMapper.class);
	
	@Before
	public void setup() {
		
		asciiArtService.display("SETUP");
		
		log.info(" > Local server port : {}", port);
	}
	
	@Test
	public void action() {
		
		asciiArtService.display("ACTION");
		
		List<SupplierEntity> suppliers = supplierRepository.findBySupplierIdNotNull(PageRequest.of(0, 10, Sort.by(Direction.ASC, "supplierId"))).
				collectList().block();
			assertThat(suppliers).isNotEmpty();
		
		//Find by name.
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>(1);
		params.add("name", suppliers.stream().findFirst().get().getName());
		
		getAndVerifyStatus(params, HttpStatus.OK);
		
		//Supplier creation.
		createAndVerifyStatus(Supplier.builder().amountOfCapital(10000L).creationDate(LocalDateTime.now()).
				fiscalCountryCode("001").fiscalId(fiscalIdService.getId()).legalId(fiscalIdService.getId()).
				name("FACOM_" + supplierIdService.getId()).
				supplierType(SupplierEntity.Type.SUPPLIER.name()).build(), 
				HttpStatus.CREATED);
		
		//Supplier update.
		List<SupplierEntity> facomSuppliers = supplierRepository.findByNameStartingWith("FACOM_", PageRequest.of(0, 2, Sort.by(Direction.ASC, "supplierId"))).
				collectList().block();
		
		assertTrue(facomSuppliers.size() == 1);
		
		SupplierEntity firstFacomSupplier = facomSuppliers.stream().findFirst().get();
		firstFacomSupplier.setOtherName("FACOM_POWER");
		
		updateAndVerifyStatus(mapper.toModel(firstFacomSupplier), HttpStatus.OK);
	}
	
	/**
	 * @param params
	 * @param status
	 * @return {@link BodyContentSpec}
	 */
	private BodyContentSpec getAndVerifyStatus(MultiValueMap<String, String> params, HttpStatus status) {
	
		return client.get().uri(uri -> uri.pathSegment("api", "v1", Api.SUPPLIER_PATH).build()).
			accept(MediaType.APPLICATION_JSON).exchange().
			expectStatus().isEqualTo(status).
			expectBody();
	}
	
	/**
	 * @param supplier
	 * @param status
	 * @return {@link BodyContentSpec}
	 */
	private BodyContentSpec createAndVerifyStatus(Supplier supplier, HttpStatus status) {
		
		return client.post().uri(uri -> uri.pathSegment("api", "v1", Api.SUPPLIER_PATH).build()).
			body(Mono.just(supplier), Supplier.class).
			accept(MediaType.APPLICATION_JSON).exchange().
			expectStatus().isEqualTo(status).
			expectBody();
	}
	
	/**
	 * @param supplier
	 * @param status
	 * @return {@link BodyContentSpec}
	 */
	private BodyContentSpec updateAndVerifyStatus(Supplier supplier, HttpStatus status) {
		
		return client.put().uri(uri -> uri.pathSegment("api", "v1", Api.SUPPLIER_PATH).build()).
			body(Mono.just(supplier), Supplier.class).
			accept(MediaType.APPLICATION_JSON).exchange().
			expectStatus().isEqualTo(status).
			expectBody();
	}
}
