package com.me.api.supplier.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class HttpErrorInfo {

	@JsonProperty(value = "timestamp", required = true) @NotNull
	private LocalDateTime timestamp;
	
	@JsonProperty(value = "path", required = true) @NotBlank
    private String path;
	
	@JsonProperty(value = "httpStatus", required = true) @NotNull
    private Integer httpStatus;
	
	@JsonProperty(value = "message", required = true) @NotBlank
    private String message;
}
