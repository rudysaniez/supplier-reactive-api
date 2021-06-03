package com.me.api.supplier;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.me.api.supplier.controller.config.PropertiesConfig.PaginationInformation;
import com.me.api.supplier.domain.SupplierEntity;
import com.me.api.supplier.repository.SupplierRepository;
import com.me.api.supplier.service.console.AsciiArtService;
import com.me.api.supplier.service.id.FiscalIdService;
import com.me.api.supplier.service.id.SupplierIdService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableScheduling
@EnableTransactionManagement
@EnableR2dbcRepositories
@EnableConfigurationProperties(value = {PaginationInformation.class})
@ComponentScan(basePackages = "com.me.api.supplier")
@SpringBootApplication
public class Application {

	@Profile("test")
	@Bean
	public EmbeddedDatabase datasource() {
		
		return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).
				setName("suppliersdb").
				build();
	}
	
	public static void main(String[] args) {
		
		SpringApplication app = new SpringApplication(Application.class);
		app.setWebApplicationType(WebApplicationType.REACTIVE);
		app.setBannerMode(Mode.CONSOLE);
		app.run(args);
	}
	
	/**
	 * @param artService
	 * @param supplierRepository
	 * @param supplierIdService
	 * @return {@link ApplicationRunner}
	 */
	@Bean @Autowired @Profile({"dev-local", "cloud", "liquibase-cloud"})
	public ApplicationRunner run(AsciiArtService artService, SupplierRepository supplierRepository, 
			SupplierIdService supplierIdService, FiscalIdService fiscalIdService) {
		
		return args -> {
			
			artService.display("APPLICATION RUNNER");
			
			if(args.containsOption("cleaning")) {
				
				supplierRepository.deleteAll().block();
				log.info(" > Cleaning is done.");
			}
			
			log.info(" > Number of suppliers : {}", supplierRepository.count().block());
			
			if(args.containsOption("creation-on-startup")) {
				
				SupplierEntity supplierCreated = supplierRepository.save(new SupplierEntity(null, supplierIdService.getId(), 
						"DEXTER", "001", fiscalIdService.getId(), LocalDateTime.now())).block();
				
				log.info(" > Supplier has been created : {}", supplierCreated.toString());
			}
			
		};
	}
}
