package com.leansoft.mxjc.model;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * Type model for codegen
 * 
 * @author bulldog
 *
 */
public class TypeInfo implements Serializable {
	
	private static final long serialVersionUID = 7174500180688531754L;

	// simple name of this type
	private String name;
	
	// full name(fqn) of type
	private String fullName;
	
	// is this a primitive type
	private boolean isPrimitive = false;
	
	// is this an enum type
	private boolean isEnum = false;
	
	// is this an array type
	private boolean isArray = false;
	
	// is collection
	private boolean isCollection = false;
	
	// component type of the array(if this is an array type) 
	private TypeInfo elementType = null;
	
	// is this a nest class?
	private boolean nestClass = false;
	

	// type parameters of this type
	private final List<TypeInfo> typeParameters = new ArrayList<TypeInfo>() ;
	
	/**
	 * whether this is an enum type
	 * @return whether this is an enum type
	 */
	public boolean isEnum() {
		return isEnum;
	}
	
	/**
	 * set whether this is an enum type
	 * @param isEnum
	 */
	public void setEnum(boolean isEnum) {
		this.isEnum = isEnum;
	}
	
	/**
	 * whether this is an array type
	 * @return whether this is an array type
	 */
	public boolean isArray() {
		return isArray;
	}

	/**
	 * set whether this is an array type
	 * @param isArray
	 */
	public void setArray(boolean isArray) {
		this.isArray = isArray;
	}

	/**
	 * component type of the array(if this is an array type)
	 * T of T[]
	 * @return TypeInfo instance
	 */
	public TypeInfo getElementType() {
		return elementType;
	}

	/**
	 * set component type of the array(if this is an array type)
	 * @param elementType
	 */
	public void setElementType(TypeInfo elementType) {
		this.elementType = elementType;
	}

	/**
	 * whether this is a primitive type
	 * @return whether this is a primitive type
	 */
	public boolean isPrimitive() {
		return isPrimitive;
	}

	/**
	 * set whether this is a primitive type
	 * @param isPrimitive
	 */
	public void setPrimitive(boolean isPrimitive) {
		this.isPrimitive = isPrimitive;
	}

	/**
	 * simple name of this type
	 * @return simple name
	 */
	public String getName() {
		return name;
	}

	/**
	 * set simple name of this type
	 * @param simple name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * full name(fqn) of this type
	 * @return full name
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * set full name(fqn) of this type
	 * @param fullName
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * type paramters of this type
	 * @return a list of {@link TypeInfo} instances
	 */
	public List<TypeInfo> getTypeParameters() {
		return typeParameters;
	}
	
	/**
	 * whether this field is a collection
	 * @return whether this field is a collection
	 */
	public boolean isCollection() {
		return isCollection;
	}
	/**
	 * set whether this field is a collection
	 * @param isCollection
	 */
	public void setCollection(boolean isCollection) {
		this.isCollection = isCollection;
	}
	
	/**
	 * Is this a nest class or not
	 * 
	 * @return true if nest class, false otherwise 
	 */
	public boolean isNestClass() {
		return nestClass;
	}

	/**
	 * Set is this a nest class or not
	 * 
	 * @param nestClass
	 */
	public void setNestClass(boolean nestClass) {
		this.nestClass = nestClass;
	}
	
}
