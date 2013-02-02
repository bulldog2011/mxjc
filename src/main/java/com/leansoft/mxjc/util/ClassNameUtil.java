package com.leansoft.mxjc.util;

import java.io.File;

/**
 * Utility to for class/package name handling
 * 
 * @author bulldog
 *
 */
public final class ClassNameUtil {
	
	/**
	 * Strip qualifier of a fully qualified class name,
	 * 
	 * @param className, fully qualified class name, such as java.util.List
	 * @return qualified stripped class name, such List
	 */
    public static String stripQualifier(String className) {
        String qual = getQualifier(className);
        if (qual == null) return className;
        int len = className.length();
        return className.substring(qual.length() + 1, len);
    }

    /**
     * 
     * Get package name a fully qualified class name
     * 
     * @param className, fully qualified class anme, such as java.util.List
     * @return package name, such as java.util
     */
    public static String getPackageName(String className) {
        String packageName = getQualifier(className);
        return packageName != null ? packageName : "";
    }
	
	/**
	 * Get qualifier of a class name
	 * 
	 * @param className, full qualified class name, such as java.util.List
	 * @return String qualifier, such as java.util
	 */
    public static String getQualifier(String className) {
    	String name = className;
    	if (name.indexOf('<') >= 0) {//is generic class?
    		name = erase(name);
    	}
        int index = name.lastIndexOf('.');
        return (index < 0) ? null : name.substring(0, index);
    }
    
    /**
     *
     * Erase type parameters of a generic class
     *
     * @param className Generic class name, such as java.util.List<java.lang.String>
     * @return type parameters erased class name, such as java.util.List
     */
    public static String erase(String className) {
       int index = className.indexOf('<');
       if(index < 0)
           return className;
       return (index > 0)?className.substring(0, index):className;
    }
    
    /**
     * change package name into a relative file path
     * @param pkgName, the name of the package
     * @return relative file path
     */
    public static String packageNameToPath(String pkgName) {
    	if (pkgName == null) return null;
    	String path = pkgName.replace('.', File.separatorChar);
    	return path;
    }

}
