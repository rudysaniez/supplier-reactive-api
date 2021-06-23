package com.me.api.supplier;

public interface Api {

	public static final String SUPPLIER_PATH = "suppliers";
	
	public static final String SECURITY_SUPPLIER_PATH = "/suppliers";
	public static final String SECURITY_SUPPLIER_PATH_INCLUDED = "/suppliers/**";
	
	public static final String SECURITY_SUPPLIER_FUNCTION_PATH = "/suppliers-function";
	public static final String SECURITY_SUPPLIER_FUNCTION_PATH_INCLUDED = "/suppliers-function/**";
}
