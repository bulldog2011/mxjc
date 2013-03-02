package com.leansoft.mxjc.module.pico;

/**
 * 
 * 
 * @author bulldog
 *
 */
public abstract class PicoType {
	

	private static final String PICO_PREFIX = "PICO_TYPE_";
	
	// primitives
	public static final String INTEGER = PICO_PREFIX + "INT";
	
	public static final String BOOL = PICO_PREFIX + "BOOL";
	
	public static final String BYTE = PICO_PREFIX + "BYTE";
	
	public static final String CHAR = PICO_PREFIX + "CHAR";
	
	public static final String SHORT = PICO_PREFIX + "SHORT";
	
	public static final String LONG = PICO_PREFIX + "LONG";
	
	public static final String FLOAT = PICO_PREFIX + "FLOAT";
	
	public static final String DOUBLE = PICO_PREFIX + "DOUBLE";
	
	public static final String STRING = PICO_PREFIX + "STRING";
	
	public static final String ENUM = PICO_PREFIX + "ENUM";
	
	public static final String DATA = PICO_PREFIX + "DATA";
	
	// extended primitives
	public static final String DATE = PICO_PREFIX + "DATE";
	
	public static final String DURATION = PICO_PREFIX + "DURATION";
	
	public static final String QNAME = PICO_PREFIX + "QNAME";
	
	public static final String ANYELEMENT = PICO_PREFIX +"ANYELEMENT";
	
	// object
	public static final String OBJECT = PICO_PREFIX + "OBJECT";

}
