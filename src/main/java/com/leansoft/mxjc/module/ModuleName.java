package com.leansoft.mxjc.module;

public enum ModuleName {

	NANO("nano"),  // android java
	MICRO("micro"), // ios objective-c
	PICO("pico");  // php
	
	private final String name;
	
	private ModuleName(String n) {
		name = n;
	}
	
	public String toString() {
		return name;
	}
	
}
