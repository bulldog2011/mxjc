package com.leansoft.mxjc.model;

import java.io.Serializable;

/**
 * Enum constant model for codegen
 * 
 * @author bulldog
 *
 */
public class EnumConstantInfo implements Serializable {
	
	private static final long serialVersionUID = -8191948715005132615L;
	
	// simple name of this enum constant
	private String name;
	// doc comment of this enum constant
	private String docComment;
	
	// simple value of this enum constant
	private String value;
	
	/**
	 * value of this enum constant
	 * @return, value of this enum constant
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * set value of this enum constant
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * simple name of this enum constant
	 * @return, simple name of this enum constant
	 */
	public String getName() {
		return name;
	}
	/**
	 * set the simple name of this enum constant
	 * @param simple name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * doc comment of this enum constant
	 * @return doc comment
	 */
	public String getDocComment() {
		return docComment;
	}
	/**
	 * set doc comment of this enum constant
	 * @param docComment
	 */
	public void setDocComment(String docComment) {
		this.docComment = docComment;
	}
	
}
