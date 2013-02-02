package com.leansoft.mxjc.util;

public class StringUtil {
	
	/**
	 * Check if a string is empty
	 * @param string
	 * @return true or false
	 */
	public static boolean isEmpty(String string) {
		if (string == null) return true;
		if (string.trim().length() == 0) return true;
		return false;
	}

}
