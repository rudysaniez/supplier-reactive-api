package com.me.api.supplier;

public interface Scope {

	public static final String MONITOR = "rio:monitor";
	public static final String REGISTER = "rio:register";
	public static final String WRITER = "rio:writer";
	public static final String MANAGE = "rio:manage";
	
	public static final String SECURITY_SCOPE_MONITOR = "SCOPE_rio:monitor";
	public static final String SECURITY_SCOPE_REGISTER = "SCOPE_rio:register";
	public static final String SECURITY_SCOPE_WRITER = "SCOPE_rio:writer";
	public static final String SECURITY_SCOPE_MANAGE = "SCOPE_rio:manage";
}
