package com.leansoft.mxjc.model;

import com.leansoft.mxjc.model.annotation.AttributeAnnotation;
import com.leansoft.mxjc.model.annotation.ElementAnnotation;

/**
 * Field model for codegen
 * 
 * @author bulldog
 *
 */
public class FieldInfo {

	// name of this field
	private String name;
	
	// type of this field
	private TypeInfo type;
	
	// doc comments
	private String docComment;
	
	// is schema element, map to xml element
	private boolean isElement = false;
	// is schema attribute, map to xml attribute
	private boolean isAttribute = false;
	// is schema value, map to xml text value
	private boolean isValue = false;
	// is schema any, map to any
	private boolean isAny = false;
	
	private ElementAnnotation elementAnnotation;
	
	private AttributeAnnotation attributeAnnotation;
	
	/**
	 * doc comment of this field
	 * @return doc comment
	 */
	public String getDocComment() {
		return docComment;
	}
	/**
	 * set doc comment of this field
	 * @param docComment
	 */
	public void setDocComment(String docComment) {
		this.docComment = docComment;
	}
	/**
	 * simple name of this field
	 * @return simple name
	 */
	public String getName() {
		return name;
	}
	/**
	 * set simple name of this field
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * type of this field
	 * @return {@link TypeInfo} instance
	 */
	public TypeInfo getType() {
		return type;
	}
	/**
	 * set type of this field
	 * @param type
	 */
	public void setType(TypeInfo type) {
		this.type = type;
	}
	/**
	 * whether this field maps to xml element 
	 * @return whether this filed maps to xml element
	 */
	public boolean isElement() {
		return isElement;
	}
	/**
	 * set whether this field maps to xml element
	 * @param isElement
	 */
	public void setElement(boolean isElement) {
		this.isElement = isElement;
	}
	/**
	 * whether this field maps to xml attribute
	 * @return whether this field maps to xml attribute
	 */
	public boolean isAttribute() {
		return isAttribute;
	}
	/**
	 * set whether this field maps to xml attribute
	 * @param isAttribute
	 */
	public void setAttribute(boolean isAttribute) {
		this.isAttribute = isAttribute;
	}
	/**
	 * whether this field maps to xml text value
	 * @return whether this field maps to xml text value
	 */
	public boolean isValue() {
		return isValue;
	}
	/**
	 * set whether this field maps to xml text value
	 * @param isValue
	 */
	public void setValue(boolean isValue) {
		this.isValue = isValue;
	}
	/**
	 * whether this field maps to xml any
	 * @return whether this field maps to xml any
	 */
	public boolean isAny() {
		return isAny;
	}
	/**
	 * set whether this field maps to xml any
	 * @param isAny
	 */
	public void setAny(boolean isAny) {
		this.isAny = isAny;
	}
	
	public ElementAnnotation getElementAnnotation() {
		return elementAnnotation;
	}
	
	public void setElementAnnotation(ElementAnnotation elementAnnotation) {
		this.elementAnnotation = elementAnnotation;
	}
	
	public AttributeAnnotation getAttributeAnnotation() {
		return attributeAnnotation;
	}
	
	public void setAttributeAnnotation(AttributeAnnotation attributeAnnotation) {
		this.attributeAnnotation = attributeAnnotation;
	}	
}
