package com.leansoft.mxjc.module;

/**
 * Exception may thrown by client module
 * 
 * @author bulldog
 *
 */
public class XjcModuleException extends Exception {
	private static final long serialVersionUID = 1L;

	public XjcModuleException() {
	}

	public XjcModuleException(String message) {
		super(message);
	}

	public XjcModuleException(Throwable cause) {
		super(cause);
	}

	public XjcModuleException(String message, Throwable cause) {
		super(message, cause);
	}
}
