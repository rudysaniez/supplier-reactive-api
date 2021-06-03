package com.me.api.supplier.test;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.data.r2dbc.AutoConfigureDataR2dbc;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.BodyContentSpec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.me.api.supplier.Api;
import com.me.api.supplier.controller.SupplierController;
import com.me.api.supplier.controller.config.PropertiesConfig.PaginationInformation;
import com.me.api.supplier.domain.SupplierEntity;
import com.me.api.supplier.repository.SupplierRepository;
import com.me.api.supplier.service.console.AsciiArtService;

import reactor.core.publisher.Mono;

@WebFluxTest(controllers = {SupplierController.class})
@AutoConfigureDataR2dbc
@ExtendWith(SpringExtension.class)
public class SupplierServiceTest {

	@MockBean SupplierRepository supplierRepository;
	@MockBean PaginationInformation pagination;
	
	@Autowired ObjectMapper jack;
	@Autowired WebTestClient client;
	
	@Autowired private AsciiArtService asciiArtService;
	
	@Value("classpath:supplier-entity/supplier.json")
	Resource supplierJson;
	
	static final Integer PAGE_NUMBER = 0;
	static final Integer PAGE_SIZE = 20;
	static final Pageable PAGE = PageRequest.of(PAGE_NUMBER, PAGE_SIZE, Sort.by(Direction.ASC, "supplierId"));
	static final String SUPPLIER_ID = "SUP_FR_066@66431285";
	
	@BeforeEach
	public void setup() throws IOException {
		
		Mockito.when(pagination.getDefaultPage()).thenReturn(PAGE_NUMBER);
		Mockito.when(pagination.getDefaultSize()).thenReturn(PAGE_SIZE);
		
		SupplierEntity supplierEntity = jack.readValue(supplierJson.getFile(), SupplierEntity.class);
		Mockito.when(supplierRepository.findBySupplierId(SUPPLIER_ID)).thenReturn(Mono.just(supplierEntity));
	}
	
	@Test
	public void getBySupplierId() {
		
		asciiArtService.display("GET SUPPLIER ID");
		
		getAndVerifyStatus(SUPPLIER_ID, HttpStatus.OK);
	}
	
	/**
	 * @param id
	 * @param status
	 * @return {@link BodyContentSpec}
	 */
	private BodyContentSpec getAndVerifyStatus(String id, HttpStatus status) {
		
		// @formatter:off
		return client.
				get().uri(uri -> uri.pathSegment(Api.SUPPLIER_PATH, id).build()).
				accept(MediaType.APPLICATION_JSON).exchange().
				expectStatus().isEqualTo(status).
				expectBody();
		// @formatter:on
	}
	
}
