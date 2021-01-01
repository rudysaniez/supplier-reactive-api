package com.me.api.supplier.controller.exception;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.me.api.supplier.exception.NotFoundException;
import com.me.api.supplier.model.HttpErrorInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class MainControllerExceptionHandler {

	/**
	 * @param request
	 * @param exception
	 * @return {@link HttpErrorInfo}
	 */
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ExceptionHandler(value = NotFoundException.class)
	public @ResponseBody HttpErrorInfo notFoundException(ServerHttpRequest request, Exception exception) {
		return createHttpErrorInfo(HttpStatus.NOT_FOUND, request, exception);
	}
	
	/**
	 * @param httpStatus
	 * @param request
	 * @param ex
	 * @return {@link HttpErrorInfo}
	 */
	private HttpErrorInfo createHttpErrorInfo(HttpStatus httpStatus, ServerHttpRequest request, Exception ex) {
		
        final String path = request.getPath().pathWithinApplication().value();
        final String message = ex.getMessage();

    	log.debug(" > Returning HTTP status: {} for path: {}, message: {}", httpStatus, path, message);
        
        return new HttpErrorInfo(ZonedDateTime.now(), path, httpStatus, message);
    }
}
