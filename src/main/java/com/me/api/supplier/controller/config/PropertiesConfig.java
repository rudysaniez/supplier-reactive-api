package com.me.api.supplier.controller.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
public class PropertiesConfig {

	@Getter @Setter
	@ConfigurationProperties(prefix = "pagination")
	public static class PaginationInformation {
		
		private Integer defaultPage;
		private Integer defaultSize;
	}
}
