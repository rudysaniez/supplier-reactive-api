package com.me.api.supplier.service.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.me.api.supplier.bo.SupplierEntity;
import com.me.api.supplier.repository.SupplierRepository;
import com.me.api.supplier.service.id.FiscalIdService;
import com.me.api.supplier.service.id.SupplierIdService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CreateSupplierService {

	@Autowired private SupplierRepository supplierRepository;
	@Autowired private FiscalIdService fiscalIdService;
	@Autowired private SupplierIdService supplierIdService;
	
	@Scheduled(fixedRate = 15000)
	public void schedule() {
		
		String supplierId = supplierIdService.getId();
		
		SupplierEntity supplierCreated = supplierRepository.save(new SupplierEntity(null, supplierId, 
				"DEXTER_" + supplierIdService.getSupplierId(), "001", fiscalIdService.getId(), 
				SupplierEntity.Type.SUPPLIER.name())).block();
		
		log.info(" > Supplier has been created : {}", supplierCreated.toString());
	}
}
