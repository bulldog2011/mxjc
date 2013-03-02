package com.leansoft.mxjc.module;

public enum ModuleName {

	NANO("nano"),  // android java
	MICRO("micro"), // php
	PICO("pico");  // ios objective-c
	
	private final String name;
	
	private ModuleName(String n) {
		name = n;
	}
	
	public String toString() {
		return name;
	}
	
}
