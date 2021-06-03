package com.me.api.supplier.model;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ServerWebExchange;

import io.swagger.annotations.ApiParam;
import reactor.core.publisher.Mono;

@Validated
public interface SupplierApi {

	/**
	 * @param supplierId
	 * @param exchange
	 * @return {@link Supplier}
	 */
	@GetMapping(value = com.me.api.supplier.Api.SUPPLIER_PATH + "/{supplierId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<Supplier>> getBySupplierId(@ApiParam(value = "Supplier identifier.", required = true) @PathVariable(name = "supplierId", required = true) String supplierId, ServerWebExchange exchange);

	/**
	 * @param name
	 * @param page
	 * @param size
	 * @return {@link PagedSupplier}
	 */
	@GetMapping(value = com.me.api.supplier.Api.SUPPLIER_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<PagedSupplier>> findByParam(@ApiParam(value = "Supplier name", required = false) @RequestParam(name = "name", required = false) String name,
			@ApiParam(value = "Page number", required = false) @RequestParam(name = "page", required = false) Integer page, 
			@ApiParam(value = "Page size", required = false) @RequestParam(name = "size", required = false) Integer size,
			ServerWebExchange exchange);

	/**
	 * @param supplier
	 * @return
	 */
	@RequestMapping(value = com.me.api.supplier.Api.SUPPLIER_PATH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE,
			method = {RequestMethod.POST, RequestMethod.PUT})
	public Mono<ResponseEntity<Supplier>> saveOrUpdate(@Valid @RequestBody Mono<Supplier> supplier, ServerWebExchange exchange);
	
	/**
	 * @param supplierId
	 * @return {@link Void}
	 */
	@DeleteMapping(value = com.me.api.supplier.Api.SUPPLIER_PATH + "/{supplierId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<Void>> delete(@ApiParam(value = "Supplier identifier.", required = true) @PathVariable(name = "supplierId", required = true) String supplierId, ServerWebExchange exchange); 
}
