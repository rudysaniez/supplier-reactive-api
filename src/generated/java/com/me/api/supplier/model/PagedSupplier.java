package com.me.api.supplier.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PagedSupplier {

	@JsonProperty(value = "content")
	@Valid @Builder.Default
	private List<Supplier> content = new ArrayList<>();

	@JsonProperty(value = "page", required = true)
	private PageMetadata page;
	
	/**
	 * @param supplier
	 * @return {@link PagedSupplier}
	 */
	public PagedSupplier addContent(Supplier supplier) {
		
		content.add(supplier);
		return this;
	}
}
