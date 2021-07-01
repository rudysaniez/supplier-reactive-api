package com.me.api.supplier.controller.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.me.api.supplier.controller.router.handler.SupplierHandler;

@Configuration
public class SupplierRouterFunction {

	private SupplierHandler handler;
	
	public SupplierRouterFunction(SupplierHandler handler) {
		this.handler = handler;
	}
	
	@Bean
	public RouterFunction<ServerResponse> supplierRouter() {
		
		return RouterFunctions.route(RequestPredicates.GET("/suppliers/{supplierId}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), 
				handler::getBySupplierId).
				
				andRoute(RequestPredicates.GET("/suppliers").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), 
						handler::getManySuppliers).
				
				andRoute(RequestPredicates.POST("/suppliers").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), 
						handler::saveOfUpdate).
				
				andRoute(RequestPredicates.DELETE("/suppliers/{supplierId}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), 
						handler::delete);
	}
}
