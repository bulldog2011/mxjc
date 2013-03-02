package com.leansoft.mxjc.module.pico;

import java.util.HashMap;
import java.util.Map;

/**
 * Objective-c primitive to wrapper type mapping
 * 
 * @author bulldog
 *
 */
public class PicoTypeMapper {
	
	private static Map<String, String> mapping;
	
	static {
		initMapping();
	}
	
	private static void initMapping() {
		mapping = new HashMap<String, String>();
		
		mapping.put(PicoType.INTEGER, OCWrapper.NSNUMBER);
		mapping.put(PicoType.BOOL, OCWrapper.NSNUMBER);
		mapping.put(PicoType.BYTE, OCWrapper.NSNUMBER);
		mapping.put(PicoType.CHAR, OCWrapper.NSSTRING);
		mapping.put(PicoType.SHORT, OCWrapper.NSNUMBER);
		mapping.put(PicoType.LONG, OCWrapper.NSNUMBER);
		mapping.put(PicoType.FLOAT, OCWrapper.NSNUMBER);
		mapping.put(PicoType.DOUBLE, OCWrapper.NSNUMBER);
		mapping.put(PicoType.ENUM, OCWrapper.NSSTRING);
		mapping.put(PicoType.DATE, OCWrapper.NSDATE);
		mapping.put(PicoType.DURATION, OCWrapper.NSSTRING);
		mapping.put(PicoType.STRING, OCWrapper.NSSTRING);
		mapping.put(PicoType.DATA, OCWrapper.NSDATA);
		mapping.put(PicoType.QNAME, OCWrapper.PICO_QNAME);
		mapping.put(PicoType.ANYELEMENT, OCWrapper.PICO_ANYELEMENT);
	}
	
	/**
	 * Given an objective-c primitive type, return its wrapper type
	 */
	public static String lookupWrapper(String primType) {
		return mapping.get(primType);
	}

}
