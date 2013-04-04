package com.leansoft.mxjc.model;

/**
 * Config for code generation
 * 
 * @author bulldog
 *
 */
public class CGConfig {
	
	public String picoPrefix;
	
	public String picoServiceGroup;
	
	public boolean nanoPrivateField;
	
	// for demo
	public boolean eBaySOAService;
	public boolean eBayShoppingAPI;
	public boolean eBayTradingAPI;
	
	public boolean isNanoPrivateField() {
		return nanoPrivateField;
	}
}
