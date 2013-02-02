package com.leansoft.mxjc.builder;

import com.leansoft.mxjc.model.CGModel;
import com.sun.tools.xjc.ErrorReceiver;
import com.sun.tools.xjc.outline.Outline;
import com.sun.tools.xjc.reader.xmlschema.bindinfo.BindInfo;
import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSComponent;

/**
 * A model builder which can build a intermediate code generation model from
 * jaxb model.
 * 
 * <p>
 * intermediate code generation model could be considered the core of the
 * code-generator architecture, it contains a set of classes that make it easy
 * to manipulate the information coming from the jaxb model to generate
 * target code.
 * 
 * @author bulldog
 *
 */
public class ModelBuilder {
	
	
	public static CGModel buildCodeGenModel(Outline outline, ErrorReceiver errorReceiver) {
		
		CGModel cgModel = new CGModel();
		
		if (errorReceiver != null)
			errorReceiver.debug("Building class model ...");
		// build class/bean model
		ClassModelBuilder.buildClassModel(outline, cgModel, errorReceiver);
		
		if (errorReceiver != null)
			errorReceiver.debug("Building enum model ...");
		// build enum model
		EnumModelBuilder.buildEnumModel(outline, cgModel, errorReceiver);
		
		return cgModel;
	}
	
	/**
	 * Helper to get xsdoc from schema component
	 * 
	 * @param xsComp
	 *            ,XSComponent
	 * @return doc string
	 */
	public static String getDocumentation(XSComponent xsComp) {
		if (xsComp == null)
			return null;
		XSAnnotation xsa = xsComp.getAnnotation();
		if (xsa != null && xsa.getAnnotation() != null) {
			String docComment = ((BindInfo) xsa.getAnnotation())
					.getDocumentation();
			return docComment;
		}
		return null;
	}
}
