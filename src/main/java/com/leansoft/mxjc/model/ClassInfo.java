package com.leansoft.mxjc.model;

import java.util.ArrayList;
import java.util.List;

import com.leansoft.mxjc.model.nano.RootElementAnnotation;
import com.leansoft.mxjc.model.nano.XmlTypeAnnotation;

/**
 * Class model for codegen
 * 
 * @author bulldog
 *
 */
public class ClassInfo {
	
	// package name of this class
	private String packageName;
	// simple name of this class
	private String name;
	// full name of this class
	private String fullName;
	// is this an abstract class
	private boolean isAbstract;
	
	// the super class this class extends
	private TypeInfo superClass;
	// doc comment of this class
	private String docComment;
	
	// is this a nest class?
	private boolean nestClass = false;
	
	private RootElementAnnotation rootElementAnnotation;
	
	private XmlTypeAnnotation xmlTypeAnnotation;
	
	// fields of this class
	private final List<FieldInfo> fields = new ArrayList<FieldInfo>();
	
	
	/**
	 * A list of fields this class contains
	 * 
	 * @return a list of {@link FieldInfo} instances
	 */
	public List<FieldInfo> getFields() {
		return fields;
	}
	
	/**
	 * the package name of this class
	 * 
	 * @return package name
	 */
	public String getPackageName() {
		return packageName;
	}
	
	/**
	 * set the package name of this class
	 * @param packageName
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	/**
	 * get simple name of this class
	 * 
	 * @return simple name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * set simple name of this class
	 * 
	 * @param simple name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * get full name of this class
	 * 
	 * @return full name
	 */
	public String getFullName() {
		return fullName;
	}
	
	/**
	 * set full name of this class
	 * 
	 * @param fullName, the full name of this class
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	/**
	 * abstract class or not
	 * 
	 * @return abstract class or not
	 */
	public boolean isAbstract() {
		return isAbstract;
	}
	
	/**
	 * set abstract class or not
	 * 
	 * @param isAbstract
	 */
	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}
	
	/**
	 * get super class type if this class extends any super class
	 * 
	 * @return super class type, null if this class does not explicitly extend any super class
	 */
	public TypeInfo getSuperClass() {
		return superClass;
	}
	
	/**
	 * set super class type if this class extends any super class
	 * 
	 * @param superClass
	 */
	public void setSuperClass(TypeInfo superClass) {
		this.superClass = superClass;
	}
	
	/**
	 * doc comment of this class, extracted from xsd annotation
	 * @return doc comment
	 */
	public String getDocComment() {
		return docComment;
	}
	
	/**
	 * set doc comment of this class
	 * 
	 * @param docComment
	 */
	public void setDocComment(String docComment) {
		this.docComment = docComment;
	}
	
	public RootElementAnnotation getRootElementAnnotation() {
		return rootElementAnnotation;
	}

	public void setRootElementAnnotation(RootElementAnnotation rootElementAnnotation) {
		this.rootElementAnnotation = rootElementAnnotation;
	}
	
	public XmlTypeAnnotation getXmlTypeAnnotation() {
		return xmlTypeAnnotation;
	}

	public void setXmlTypeAnnotation(XmlTypeAnnotation xmlTypeAnnotation) {
		this.xmlTypeAnnotation = xmlTypeAnnotation;
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
