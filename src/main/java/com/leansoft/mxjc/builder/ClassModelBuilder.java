package com.leansoft.mxjc.builder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import com.leansoft.mxjc.model.CGModel;
import com.leansoft.mxjc.model.ClassInfo;
import com.leansoft.mxjc.model.FieldInfo;
import com.leansoft.mxjc.model.TypeInfo;
import com.leansoft.mxjc.model.annotation.AttributeAnnotation;
import com.leansoft.mxjc.model.annotation.ElementAnnotation;
import com.leansoft.mxjc.model.annotation.RootElementAnnotation;
import com.leansoft.mxjc.model.annotation.XmlTypeAnnotation;
import com.leansoft.mxjc.util.ClassNameUtil;
import com.leansoft.mxjc.util.StringUtil;
import com.sun.codemodel.ClassType;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JType;
import com.sun.tools.xjc.ErrorReceiver;
import com.sun.tools.xjc.model.CAttributePropertyInfo;
import com.sun.tools.xjc.model.CElementInfo;
import com.sun.tools.xjc.model.CElementPropertyInfo;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.model.CTypeRef;
import com.sun.tools.xjc.outline.Aspect;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;
import com.sun.xml.bind.api.impl.NameConverter;
import com.sun.xml.bind.v2.model.core.PropertyKind;
import com.sun.xml.xsom.XSComponent;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSTerm;

public class ClassModelBuilder {
    
    public static void buildClassModel(Outline outline, CGModel cgModel, ErrorReceiver errorReceiver) {
    	
		// build class full name to element qname mapping
		Map<String, QName> mapping = buildClass2ElementMapping(outline);
    	
		for (ClassOutline co : outline.getClasses()) {
			ClassInfo classInfo = new ClassInfo();
			// package name of the class
//			classInfo.setPackageName(co._package()._package().name());
			String pkgName = ClassNameUtil.getPackageName(co.implClass.fullName());
			// for anonymous inner class, we need to change package name to lower case
			if (isNestClass(co.implClass)) {
				classInfo.setNestClass(true);
			}
			classInfo.setPackageName(pkgName);
			// simple name of the class
			classInfo.setName(co.implClass.name());
			// full name of the class
			if (!StringUtil.isEmpty(classInfo.getPackageName())) {
				classInfo.setFullName(pkgName + "." + classInfo.getName());
			} else {
				classInfo.setFullName(classInfo.getName());
			}
			
	        // [SAMPLE RESULT]
	        // @XmlType(name="foo", targetNamespace="bar://baz")
			classInfo.setXmlTypeAnnotation(getXmlTypeAnnotation(co));
			
			// abstract class?
			classInfo.setAbstract(co.implClass.isAbstract());
			
			// has super class?
			ClassOutline sco = co.getSuperClass();
			if (sco != null) {
				TypeInfo superClass = new TypeInfo();
				superClass.setName(sco.implClass.name());
				superClass.setFullName(sco.implClass.fullName());
				classInfo.setSuperClass(superClass);
			}
			
			classInfo.setRootElementAnnotation(getRootElementAnnotation(co, mapping, classInfo));
			
			// xsd annotation as doc comment
			String docComment = ModelBuilder.getDocumentation(co.target.getSchemaComponent());
			classInfo.setDocComment(docComment);
			
			// build field model
			for (FieldOutline fo : co.getDeclaredFields()) {
				
				FieldInfo attrInfo = new FieldInfo();
				// field name
				attrInfo.setName(fo.getPropertyInfo().getName(false));
				
				// raw type of this field
				TypeInfo typeInfo = new TypeInfo();
				JType rawType = fo.getRawType();
				typeInfo.setName(rawType.name());
				
//				pkgName = ClassNameUtil.getPackageName(rawType.fullName());
				// for anonymous inner class, we need to change package name to lower case
				if (isNestClass(rawType)) {
					typeInfo.setNestClass(true);
				}
//				String typeName = ClassNameUtil.stripQualifier(rawType.fullName());
				typeInfo.setFullName(rawType.fullName());
				typeInfo.setPrimitive(rawType.isPrimitive());
				
				if (isEnum(rawType)) {
					typeInfo.setEnum(true);
				} else {
					typeInfo.setEnum(false);
				}
				
				if (rawType.isArray()) { // is array type?
					typeInfo.setArray(true);
					
					// T of T[]
					JType elementType = rawType.elementType();
					TypeInfo elementTypeInfo = new TypeInfo();
					elementTypeInfo.setName(elementType.name());
					
//					pkgName = ClassNameUtil.getPackageName(elementType.fullName());
					// for anonymous inner class, we need to change package name to lower case
					if (isNestClass(elementType)) {
						elementTypeInfo.setNestClass(true);	
					}
//					typeName = ClassNameUtil.stripQualifier(elementType.fullName());
					elementTypeInfo.setFullName(elementType.fullName());
					elementTypeInfo.setPrimitive(elementType.isPrimitive());
					if (isEnum(elementType)) {
						elementTypeInfo.setEnum(true);
					}
					typeInfo.setElementType(elementTypeInfo);
				}
				
				if (rawType instanceof JClass) { // has type parameters?
					JClass clzType = (JClass) rawType;
					for(JClass typeParamClass : clzType.getTypeParameters()) {
						TypeInfo typeParamInfo = new TypeInfo();
						typeParamInfo.setName(typeParamClass.name());
						
//						pkgName = ClassNameUtil.getPackageName(typeParamClass.fullName());
						// for anonymous inner class, we need to change package name to lower case
						if (isNestClass(typeParamClass)) {
							typeParamInfo.setNestClass(true);	
						}
//						typeName = ClassNameUtil.stripQualifier(typeParamClass.fullName());
						typeParamInfo.setFullName(typeParamClass.fullName());
						typeParamInfo.setPrimitive(typeParamClass.isPrimitive());
						if (isEnum(typeParamClass)) {
							typeParamInfo.setEnum(true);
						}
						typeInfo.getTypeParameters().add(typeParamInfo);
					}
					
//					JClass outerClass = clzType.outer();
//					if (outerClass != null) { // for anonymous type, we use package of the outer class since we treat it as stand-alone class
//						logger.info(typeInfo.getName() + " is inner class of " + outerClass.name());
//						typeInfo.setFullName(outerClass._package().name() + "." + typeInfo.getName());
//					}
				}
				attrInfo.setType(typeInfo);
				
				// schema kind
				CPropertyInfo cProp = fo.getPropertyInfo();
				if (cProp.kind() == PropertyKind.ELEMENT) {
					attrInfo.setElement(true);
				} else if (cProp.kind() == PropertyKind.ATTRIBUTE) {
					attrInfo.setAttribute(true);
				} else if (cProp.kind() == PropertyKind.VALUE) {
					attrInfo.setValue(true);
				} else if (cProp.kind() == PropertyKind.REFERENCE) {
					attrInfo.setAny(true);
				}
				
				// Annotation
				if (cProp instanceof CElementPropertyInfo) {
					CElementPropertyInfo ep = (CElementPropertyInfo) cProp;
					List<CTypeRef> types = ep.getTypes();
					if (types.size() == 1) {
						CTypeRef t = types.get(0);
						attrInfo.setElementAnnotation(getElementAnnotation(co, cProp, t));
					} else {
						if (errorReceiver != null) {
							errorReceiver.warning(ep.locator, "found xsd choice which is not supported yet");
						}
					}
				} else if (cProp instanceof CAttributePropertyInfo) {
					attrInfo.setAttributeAnnotation(getAttributeAnnotation(co, cProp));
				}
				
				
				// is collection?
				if (cProp.isCollection()) {
					attrInfo.getType().setCollection(true);
				}
				
				// doc comment
				XSComponent xsComp = fo.getPropertyInfo().getSchemaComponent();
				if (xsComp != null && xsComp instanceof XSParticle) {
					XSParticle xsParticle = (XSParticle) xsComp;
					XSTerm xsTerm = xsParticle.getTerm();
					String attrDoc = ModelBuilder.getDocumentation(xsTerm);
					attrInfo.setDocComment(attrDoc);
				}
				
				
				classInfo.getFields().add(attrInfo);
			}
			
			// add this class in the code generation model
			cgModel.getClasses().add(classInfo);
		}
    }
    
    private static boolean isNestClass(JType type) {
    	if (type instanceof JClass) {
    		JClass clazz = (JClass)type;
    		JClass out = clazz.outer();
    		if (out == null) {
    			return false;
    		}
    		if (out instanceof JClass) {
    			return true;
    		}
    	}
    	return false;
    }
    
    private static XmlTypeAnnotation getXmlTypeAnnotation(ClassOutline co) {
    	XmlTypeAnnotation xmlTypeAnnotation = new XmlTypeAnnotation();
    	
        // used to simplify the generated annotations
        String mostUsedNamespaceURI = co._package().getMostUsedNamespaceURI();
        
        QName typeName = co.target.getTypeName();
        if (typeName == null) {
        	xmlTypeAnnotation.setName(""); // TODO, handle anonymous type
        	xmlTypeAnnotation.setNamespace(mostUsedNamespaceURI);
        } else {
        	xmlTypeAnnotation.setName(typeName.getLocalPart());
            final String typeNameURI = typeName.getNamespaceURI();
//            if(!typeNameURI.equals(mostUsedNamespaceURI)) // only generate if necessary
            	xmlTypeAnnotation.setNamespace(typeNameURI);
        }
        
        return xmlTypeAnnotation;
    }
    
    private static RootElementAnnotation getRootElementAnnotation(ClassOutline co, Map<String, QName> mapping, ClassInfo classInfo) {
    	// does this type map to a global element?
    	QName elementName = null;
    	if (co.target.isElement()) {
    		elementName = co.target.getElementName();
    	} else {// TODO, need to figure out a general way to handle element to class mapping
			elementName = mapping.get(classInfo.getFullName());
    	}
    	
    	if (elementName != null) {
    		RootElementAnnotation rootElementAnnotation = new RootElementAnnotation();
    		rootElementAnnotation.setName(elementName.getLocalPart());
    		rootElementAnnotation.setNamespace(elementName.getNamespaceURI());
    		
        	return rootElementAnnotation;
    	} else {
    		return null;
    	}
    }
    
    private static AttributeAnnotation getAttributeAnnotation(ClassOutline parent, CPropertyInfo prop) {
        CAttributePropertyInfo ap = (CAttributePropertyInfo) prop;
        QName attName = ap.getXmlName();
        
        AttributeAnnotation attributeAnnotation = new AttributeAnnotation();
        
        final String generatedName = attName.getLocalPart();
        
        // Issue 570; always force generating name="" when do it when globalBindings underscoreBinding is set to non default value
        // generate name property?
        if(!generatedName.equals(ap.getName(false)) || (parent.parent().getModel().getNameConverter() != NameConverter.standard)) {
        	attributeAnnotation.setName(generatedName);
        }
        
        return attributeAnnotation;
    }
    
    private static ElementAnnotation getElementAnnotation(ClassOutline parent, CPropertyInfo prop, CTypeRef ctype) {
    	ElementAnnotation elementAnnotation = null;
    	
        String propName = prop.getName(false);
        
        // generate the name property?
        String generatedName = ctype.getTagName().getLocalPart();
        if(!generatedName.equals(propName)) {
        	elementAnnotation = new ElementAnnotation();
        	elementAnnotation.setName(generatedName);
        }
    	
    	return elementAnnotation;
    }
    
	/**
	 * check if a JType is an enum type
	 * 
	 * @param jType
	 * @return boolean
	 */
	private static boolean isEnum(JType jType) {
		if (jType instanceof JDefinedClass) { // is enum?
			JDefinedClass jDefinedClass = (JDefinedClass) jType;
			ClassType classType = jDefinedClass.getClassType();
			if (classType == ClassType.ENUM) {
				return true;
			}
		}
		return false;
	}
    
	/**
	 * Build class name to element name mapping
	 * 
	 * @param outline, JAXB schema/code model
	 * @return class name to element name map
	 */
	private static Map<String, QName> buildClass2ElementMapping(Outline outline) {
		Map<String, QName> mapping = new HashMap<String, QName>();
		for(CElementInfo ei : outline.getModel().getAllElements()) {
	        JType exposedType = ei.getContentInMemoryType().toType(outline,Aspect.EXPOSED);
	        mapping.put(exposedType.fullName(), ei.getElementName());
		}
		return mapping;
	}

}
