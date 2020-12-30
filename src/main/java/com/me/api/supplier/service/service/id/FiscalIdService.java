package com.me.api.supplier.service.service.id;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FiscalIdService {

	private String fiscalIdFormat = "00000000";
	private Integer fiscalIdMax = 999999;
	
	private static final Random random = new Random();
	public static final String DOMAIN = "FID";
	public static final String DEFAULT_SEPARATOR = "_";
	
	@Autowired
	public FiscalIdService(@Value("${strategy.id.fiscalIdFormat}") String fiscalIdFormat, 
			@Value("${strategy.id.fiscalIdMax}") Integer fiscalIdMax) {
		
		this.fiscalIdFormat = fiscalIdFormat;
		this.fiscalIdMax = fiscalIdMax;
		
		log.info(" > fiscalIdFormat={}, fiscalIdMax={}", fiscalIdFormat, fiscalIdMax);
	}
	
	/**
	 * @return ID
	 */
	public String getId() {

		String baseId = fiscalIdFormat + String.valueOf(random.nextInt(fiscalIdMax));
		log.debug(" > Base of Id : {}", baseId);
		
		String fiscalId = baseId.substring(baseId.length() - fiscalIdFormat.length());
		log.debug(" > FiscalId generated : {}", fiscalId);
		
		StringBuilder id = new StringBuilder(DOMAIN);
		id.append(DEFAULT_SEPARATOR).append(fiscalId);
		
		log.info(" > Id generated is : {}", id.toString());
		
		return id.toString();
	}
}
