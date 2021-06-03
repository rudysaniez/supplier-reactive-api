package com.me.api.supplier.service.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.me.api.supplier.repository.SupplierRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile({"dev-local", "cloud", "liquibase-cloud"})
@Service
public class NumberSupplierService {

	@Autowired private SupplierRepository supplierRepository;
	
	@Scheduled(fixedRate = 10000)
	public void schedule() {
		log.info(" > Number of suppliers : {}", supplierRepository.count().block());
	}
}
