package com.me.api.supplier.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode.Exclude;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Supplier {

	@JsonProperty(value = "supplierId", required = false)
	private String supplierId;
	
	@NotEmpty
	@JsonProperty(value = "name", required = true)
	private String name;
	
	@NotEmpty @NotNull
	@JsonProperty(value = "fiscalCountryCode", required = true)
	private String fiscalCountryCode;
	
	@NotEmpty @NotNull
	@JsonProperty(value = "fiscalId", required = true)
	private String fiscalId;
	
	@Exclude
	@JsonProperty(value = "numberValues", required = false)
	private Integer[] numberValues;
	
	@Exclude
	@JsonProperty(value = "creationDate", required = false)
	private LocalDateTime creationDate;
	
	@Exclude
	@JsonProperty(value = "updateDate", required = false)
	private LocalDateTime updateDate;
}
