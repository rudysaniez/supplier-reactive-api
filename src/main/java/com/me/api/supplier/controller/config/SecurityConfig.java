package com.me.api.supplier.controller.config;

import java.util.Arrays;

import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.me.api.supplier.Api;
import com.me.api.supplier.Management;
import com.me.api.supplier.Scope;

@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	public SecurityWebFilterChain configure(ServerHttpSecurity http) {
		
		// @formatter:off
		http.
			cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource())).
			csrf().
				disable().
			authorizeExchange().
				matchers(EndpointRequest.to(Management.INFO, Management.HEALTH)).
					permitAll().
				matchers(EndpointRequest.to(Management.ENV, Management.METRICS)).
					hasAuthority(Scope.SECURITY_SCOPE_MONITOR).
				
				pathMatchers(HttpMethod.GET, Api.SECURITY_SUPPLIER_PATH, Api.SECURITY_SUPPLIER_PATH_INCLUDED).
					hasAuthority(Scope.SECURITY_SCOPE_REGISTER).
					
				pathMatchers(HttpMethod.GET, Api.SECURITY_SUPPLIER_FUNCTION_PATH, Api.SECURITY_SUPPLIER_FUNCTION_PATH_INCLUDED).
					hasAuthority(Scope.SECURITY_SCOPE_REGISTER).
					
				pathMatchers(HttpMethod.DELETE, Api.SECURITY_SUPPLIER_PATH, Api.SECURITY_SUPPLIER_PATH_INCLUDED).
					hasAuthority(Scope.SECURITY_SCOPE_MANAGE).
					
				pathMatchers(HttpMethod.DELETE, Api.SECURITY_SUPPLIER_FUNCTION_PATH, Api.SECURITY_SUPPLIER_FUNCTION_PATH_INCLUDED).
					hasAuthority(Scope.SECURITY_SCOPE_MANAGE).
			anyExchange().
				authenticated().
			and().
				oauth2ResourceServer().
					jwt();
		// @formatter:on
		
		return http.build();
	}
	
	/**
     * @return {@link CorsConfigurationSource}
     */
    public CorsConfigurationSource corsConfigurationSource() {
    	
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setMaxAge(8000L);
        configuration.setAllowedMethods(Arrays.asList("GET","PUT","POST","DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
