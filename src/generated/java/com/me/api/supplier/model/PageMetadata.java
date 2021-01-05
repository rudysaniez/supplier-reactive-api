package com.me.api.supplier.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @Builder @AllArgsConstructor
public class PageMetadata {

	@JsonProperty(value = "size", required = true)
	private Long size;

	@JsonProperty(value = "totalElements", required = true)
	private Long totalElements;

	@JsonProperty(value = "totalPages", required = true)
	private Long totalPages;

	@JsonProperty(value = "number", required = true)
	private Long number;
}
