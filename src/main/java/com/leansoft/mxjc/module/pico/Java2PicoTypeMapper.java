package com.leansoft.mxjc.module.pico;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Date;
import java.util.Currency;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import com.sun.xml.xsom.impl.util.Uri;

/**
 * Java to Objective-c primitive type mapper
 * 
 * @author bulldog
 *
 */
public class Java2PicoTypeMapper {
	
	/**
	 * java to objc primitive type mapping
	 */
	private static Map<String, String> primitiveMap;
	
	/**
	 * java.lang to objc type mapping
	 */
	private static Map<String, String> languageMap;
	
	/**
	 * java.util to objc type mapping
	 */
	private static Map<String, String> utilityMap;
	
	/**
	 * javax.xml to objc type mapping
	 */
	private static Map<String, String> xmlMap;
	
	/**
	 * java.net to objc type mapping
	 */
	private static Map<String, String> urlMap;
	
	/**
	 * java.math to objc type mapping
	 */
	private static Map<String, String> mathMap;
	
	/**
	 * initialize java to objc primitive type mapping
	 */
	private static void initPrimitiveMap() {
		primitiveMap = new HashMap<String, String>();
		
		primitiveMap.put(int.class.getName(), PicoType.INTEGER);
		primitiveMap.put(boolean.class.getName(), PicoType.BOOL);
		primitiveMap.put(long.class.getName(), PicoType.LONG);
		primitiveMap.put(double.class.getName(), PicoType.DOUBLE);
		primitiveMap.put(float.class.getName(), PicoType.FLOAT);
		primitiveMap.put(short.class.getName(), PicoType.SHORT);
		primitiveMap.put(byte.class.getName(), PicoType.BYTE);
		primitiveMap.put(char.class.getName(), PicoType.CHAR); //string or int?
		primitiveMap.put("byte[]", PicoType.DATA);
	}

	/**
	 * initialize java.lang to objc type mapping
	 */
	private static void initLanguageMap() {
		languageMap = new HashMap<String, String>();
		
		languageMap.put(Boolean.class.getName(), PicoType.BOOL);
		languageMap.put(Integer.class.getName(), PicoType.INTEGER);
		languageMap.put(Long.class.getName(), PicoType.LONG);
		languageMap.put(Double.class.getName(), PicoType.DOUBLE);
		languageMap.put(Float.class.getName(), PicoType.FLOAT);
		languageMap.put(Short.class.getName(), PicoType.SHORT);
		languageMap.put(Byte.class.getName(), PicoType.BYTE);
		languageMap.put(Character.class.getName(), PicoType.CHAR);
		languageMap.put(String.class.getName(), PicoType.STRING);
	}
	
	
	/**
	 * initialize java.util to objc type mapping
	 */
	private static void initUtilityMap() {
		utilityMap = new HashMap<String, String>();
		
		utilityMap.put(Date.class.getName(), PicoType.DATE);
		utilityMap.put(Locale.class.getName(), PicoType.STRING);
		utilityMap.put(Currency.class.getName(), PicoType.STRING);
		utilityMap.put(GregorianCalendar.class.getName(), PicoType.DATE);
		utilityMap.put(TimeZone.class.getName(), PicoType.STRING);
	}
	
	
	/**
	 * initialize javax.xml to objc type mapping
	 */
	private static void initXMLMap() {
		xmlMap = new HashMap<String, String>();
		
		xmlMap.put(XMLGregorianCalendar.class.getName(), PicoType.DATE);
		xmlMap.put(Duration.class.getName(), PicoType.DURATION);
		xmlMap.put(QName.class.getName(), PicoType.QNAME);
		xmlMap.put(Object.class.getName(), PicoType.ANYELEMENT);
	}
	
	/**
	 * initialize java.net to objc type mapping
	 */
	private static void initUrlMap() {
		urlMap = new HashMap<String, String>();
		
		urlMap.put(URL.class.getName(), PicoType.STRING);
		urlMap.put(Uri.class.getName(), PicoType.STRING);
	}
	
	private static void initMathMap() {
		mathMap = new HashMap<String, String>();
		
		mathMap.put(BigDecimal.class.getName(), PicoType.DOUBLE);
		mathMap.put(BigInteger.class.getName(), PicoType.LONG);
	}
	
	static {
		initPrimitiveMap();
		initLanguageMap();
		initUtilityMap();
		initXMLMap();
		initUrlMap();
		initMathMap();
	}
	
	
	/**
	 * Given a java primitive type, find its mapped pico type.
	 * 
	 * @param javaType
	 * @return
	 */
	public static String lookupPicoType(String javaType) {
		if (languageMap.containsKey(javaType)) {
			return languageMap.get(javaType);			
		}
		
		if (xmlMap.containsKey(javaType)) {
			return xmlMap.get(javaType);
		}
		
		if (utilityMap.containsKey(javaType)) {
			return utilityMap.get(javaType);
		}
		
		if (primitiveMap.containsKey(javaType)) {
			return primitiveMap.get(javaType);
		}
		
		if (urlMap.containsKey(javaType)) {
			return urlMap.get(javaType);
		}
		
		if (mathMap.containsKey(javaType)) {
			return mathMap.get(javaType);
		}
		
		return null;
	}
}
