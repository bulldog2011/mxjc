package com.leansoft.mxjc.module;

public enum ModuleName {

	NANO("nano"),  // android java
	MICRO("micro"), // php
	PICO("pico"),  // ios objective-c
	PICOARC("picoarc");  // ios objective-c
	
	private final String name;
	
	private ModuleName(String n) {
		name = n;
	}
	
	public String toString() {
		return name;
	}
	
	public static ModuleName fromString(String value) {
		if (value != null) {
			for(ModuleName moduleName : ModuleName.values()) {
				if (value.equalsIgnoreCase(moduleName.name)) {
					return moduleName;
				}
			}
		}
		return null;
	}
	
}
