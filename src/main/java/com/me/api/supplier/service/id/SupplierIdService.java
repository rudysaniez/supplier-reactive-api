package com.me.api.supplier.service.id;

import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SupplierIdService {

	private String bu;
	private String storeIdFormat;
	private Integer storeIdMax;
	private String supplierIdFormat;
	private Integer supplierIdMax;
	
	private static final Random random = new Random();
	public static final String DOMAIN = "SUP";
	public static final String DEFAULT_SEPARATOR = "_";
	public static final String FINAL_SEPARATOR = "@";
	
	private static final String PREPARE_IDENTIFIER;
	
	static {
		
		StringBuilder sb = new StringBuilder(DOMAIN);
		sb.append(DEFAULT_SEPARATOR).append("%s").append(DEFAULT_SEPARATOR).append("%s").
		append(FINAL_SEPARATOR).append("%s");
		
		PREPARE_IDENTIFIER = sb.toString();
	}
	
	@Autowired
	public SupplierIdService(@Value("${strategy.id.bu}") String bu, @Value("${strategy.id.storeIdFormat}") String storeIdFormat, 
			@Value("${strategy.id.storeIdMax}") Integer storeIdMax,
			@Value("${strategy.id.supplierIdFormat}") String supplierIdFormat,
			@Value("${strategy.id.supplierIdMax}") Integer supplierIdMax) {
		
		this.bu = bu;
		this.storeIdFormat = storeIdFormat;
		this.storeIdMax = storeIdMax;
		this.supplierIdFormat = supplierIdFormat;
		this.supplierIdMax = supplierIdMax;
		
		log.info(" > Bu={}, storeIdFormat={}, storeIdMax={}, supplierIdFormat={}, supplierIdMax={}", 
				bu, storeIdFormat, storeIdMax, supplierIdFormat, supplierIdMax);
	}
	
	/**
	 * @return ID
	 */
	public String getId() {
		
		String newId = String.format(PREPARE_IDENTIFIER, getBu(), getStoreId(), getSupplierId());
		
		log.info(" > Id generated is : {}", newId);
		
		return newId;
	}
	
	/**
	 * @param storeId
	 * @return ID
	 */
	public String getId(String storeId) {
		
		if(StringUtils.isBlank(storeId)) new IllegalArgumentException("The storeId argument must not be empty.");
		
		String newId = String.format(PREPARE_IDENTIFIER, getBu(), storeId, getSupplierId());
		
		log.info(" > Id generated is : {}", newId);
		
		return newId;
	}
	
	/**
	 * @return supplierId generated
	 */
	public String getSupplierId() {
		
		String baseId = supplierIdFormat + String.valueOf(random.nextInt(supplierIdMax));
		log.debug(" > Base of Id : {}", baseId);
		
		String supplierId = baseId.substring(baseId.length() - supplierIdFormat.length());
		log.debug(" > SupplierId generated : {}", supplierId);
		
		return supplierId;
	}
	
	/**
	 * @return storeId generated
	 */
	public String getStoreId() {
		
		String baseId = new String(storeIdFormat + Integer.valueOf(random.nextInt(storeIdMax)));
		log.debug(" > Base of storeId : {}", baseId);
		
		String storeId = baseId.substring(baseId.length() - storeIdFormat.length());
		log.debug(" > StoreId generated : {}", storeId);
		
		return storeId;
	}	
	
	/**
	 * @return BU
	 */
	public String getBu() {
		return bu;
	}
}
