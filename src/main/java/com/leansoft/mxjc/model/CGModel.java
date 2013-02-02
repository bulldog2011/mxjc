package com.leansoft.mxjc.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Data model for code generation
 * 
 * @author bulldog
 *
 */
public class CGModel {
	
	private final List<ClassInfo> classes = new ArrayList<ClassInfo>();
	
	private final List<EnumInfo> enums = new ArrayList<EnumInfo>();

    /**
     *  Class model for codegen
     * @return List<ClassInfo>
     */
	public List<ClassInfo> getClasses() {
		return classes;
	}
	
    /**
     *  Enum model for codegen
     * @return List<EnumInfo>
     */
	public List<EnumInfo> getEnums() {
		return enums;
	}
	
}
