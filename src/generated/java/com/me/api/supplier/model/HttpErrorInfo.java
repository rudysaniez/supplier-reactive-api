package com.me.api.supplier.model;

import java.time.ZonedDateTime;

import javax.validation.constraints.NotEmpty;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class HttpErrorInfo {

	@JsonProperty(value = "timestamp", required = true)
	private ZonedDateTime timestamp;
	
	@NotEmpty
	@JsonProperty(value = "path", required = true)
    private String path;
	
	@JsonProperty(value = "httpStatus", required = true)
    private HttpStatus httpStatus;
	
	@NotEmpty
	@JsonProperty(value = "message", required = true)
    private String message;
}
