package com.me.api.supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.me.api.supplier.bo.SupplierEntity;
import com.me.api.supplier.repository.SupplierRepository;
import com.me.api.supplier.service.console.AsciiArtService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableScheduling
@EnableTransactionManagement
@EnableR2dbcRepositories
@ComponentScan(basePackages = "com.me.api.supplier")
@SpringBootApplication
public class Application {

	private static final String MASTER = "SUP_FR_001_00000001";
	
	public static void main(String[] args) {
		
		SpringApplication app = new SpringApplication(Application.class);
		app.setWebApplicationType(WebApplicationType.REACTIVE);
		app.run(args);
	}
	
	@Getter @Setter
	@ConfigurationProperties(prefix = "pagination")
	public static class PaginationInformation {
		
		private Integer defaultPage;
		private Integer defaultSize;
	}
	
	@Bean @Autowired
	public ApplicationRunner run(AsciiArtService artService, SupplierRepository supplierRepository) {
		
		return args -> {
			
			artService.display("APPLICATION RUNNER");
			
			log.info(" > Number of suppliers : {}", supplierRepository.count().block());
			
			SupplierEntity supplier = supplierRepository.findBySupplierId(MASTER).block();
			if(supplier == null) {
				
				supplier = supplierRepository.save(new SupplierEntity(null, MASTER, "MASTER", "001", "FID_000001", "SUPPLIER")).block();
				log.info(" > Supplier has been created : {}", supplier.toString());
			}
			else log.info(" > The supplier with id={} already exists.", MASTER);
		};
	}
}
