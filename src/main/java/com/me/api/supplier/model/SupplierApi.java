package com.me.api.supplier.model;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ServerWebExchange;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Mono;

@Validated
@Api(value = "suppliers")
public interface SupplierApi {

	/**
	 * @param supplierId
	 * @param exchange
	 * @return {@link Supplier}
	 */
	@ApiOperation(value = "Get a supplier by supplierId.", nickname = "getBySupplierId", notes = "", response = Supplier.class, tags = {"supplier-controller"})
	@ApiResponses(value = { 
	        @ApiResponse(code = 404, message = "Not Found", response = HttpErrorInfo.class),
	        @ApiResponse(code = 422, message = "Unprocessable Entity", response = HttpErrorInfo.class),
	        @ApiResponse(code = 200, message = "OK", response = Supplier.class) })
	
	@GetMapping(value = com.me.api.supplier.Api.SUPPLIER_PATH + "/{supplierId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<Supplier>> getBySupplierId(@ApiParam(value = "Supplier identifier.", required = true) @PathVariable(name = "supplierId", required = true) String supplierId, ServerWebExchange exchange);

	/**
	 * @param name
	 * @param page
	 * @param size
	 * @return {@link PagedSupplier}
	 */
	@ApiOperation(value = "Find a supplier by several parameters", nickname = "findByParam", response = PagedSupplier.class, tags = {"supplier-controller"})
	@ApiResponses(value = { 
	        @ApiResponse(code = 404, message = "Not Found", response = HttpErrorInfo.class),
	        @ApiResponse(code = 422, message = "Unprocessable Entity", response = HttpErrorInfo.class),
	        @ApiResponse(code = 200, message = "OK", response = Supplier.class) })
	
	@GetMapping(value = com.me.api.supplier.Api.SUPPLIER_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<PagedSupplier>> findByParam(@ApiParam(value = "Supplier name", required = false) @RequestParam(name = "name", required = false) String name,
			@ApiParam(value = "Page number", required = false) @RequestParam(name = "page", required = false) Integer page, 
			@ApiParam(value = "Page size", required = false) @RequestParam(name = "size", required = false) Integer size);
}
