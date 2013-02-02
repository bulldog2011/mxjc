package com.leansoft.mxjc.module.nano;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.datatype.XMLGregorianCalendar;

import com.leansoft.mxjc.model.CGModel;
import com.leansoft.mxjc.model.ClassInfo;
import com.leansoft.mxjc.model.EnumInfo;
import com.leansoft.mxjc.model.FieldInfo;
import com.leansoft.mxjc.model.FileInfo;
import com.leansoft.mxjc.model.TypeInfo;
import com.leansoft.mxjc.module.AbstractClientModule;
import com.leansoft.mxjc.module.ModuleName;
import com.leansoft.mxjc.module.XjcModuleException;
import com.leansoft.mxjc.util.ClassNameUtil;

import freemarker.template.SimpleHash;

/**
 * <h1>Nano Client Module</h1>
 * 
 * @author bulldog
 *
 */
public class NanoClientModule extends AbstractClientModule {
	
	private static final String JAVA_DEFAULT_PACKAGE_NAME = "java.lang";
	private static final String TEMPLATE_FOLDER = "template";
	
	private Map<String, String> typeMapping = new HashMap<String, String>();
	
	// references to templates
	private URL classTemplate;
	private URL enumTemplate;

	@Override
	public ModuleName getName() {
		return ModuleName.NANO;
	}

	@Override
	public void init() throws XjcModuleException {
		info("Loading templates ...");
		loadTemplates();
		
		// some custom type mappings
		// android does not fully support these data types yet
		typeMapping.put(XMLGregorianCalendar.class.getName(),
				"java.util.Date");
		typeMapping.put(javax.xml.datatype.Duration.class.getName(),
				"com.leansoft.nano.custom.types.Duration");
		
	}
	
	private void loadTemplates() throws XjcModuleException {
		classTemplate = getTemplateURL("client-class-type.fmt");
		enumTemplate = getTemplateURL("client-enum-type.fmt");
	}

	@Override
	public Set<FileInfo> generate(CGModel cgModel) throws XjcModuleException {
		// freemarker datamodel
		SimpleHash fmModel = this.getFreemarkerModel();
		
		// container for target codes
		Set<FileInfo> targetFileSet = new HashSet<FileInfo>();
		
		info("Generating the Nano client classes...");
		
		// adjust package name of nested class
		adjustPackageNameOfNestClass(cgModel.getClasses());
		
		// generate classes
		info("Generating classes ...");
		for(ClassInfo classInfo : cgModel.getClasses()) {
			this.convertFieldsType(classInfo);
			fmModel.put("clazz", classInfo);
			fmModel.put("imports", this.getClassImports(classInfo));
			String relativePath = ClassNameUtil.packageNameToPath(classInfo.getPackageName());
			FileInfo classFile = this.generateFile(classTemplate, fmModel, classInfo.getName(), "java", relativePath);
			targetFileSet.add(classFile);
		}
		
		// generate enums
		info("Generating enums ...");
		for(EnumInfo enumInfo : cgModel.getEnums()) {
			fmModel.put("enum", enumInfo);
			String relativePath = ClassNameUtil.packageNameToPath(enumInfo.getPackageName());
			FileInfo classFile = this.generateFile(enumTemplate, fmModel, enumInfo.getName(), "java", relativePath);
			targetFileSet.add(classFile);
		}
		
		return targetFileSet;
	}
	
	/**
	 * check every field of a class and convert type if necessary
	 * 
	 * @param clazz
	 *            , ClassInfo instance to be converted
	 */
	private void convertFieldsType(ClassInfo clazz) {
		for (FieldInfo field : clazz.getFields()) {
			TypeInfo fieldType = field.getType();
			convertType(fieldType);
			// convert type parameters
			for (TypeInfo paraType : fieldType.getTypeParameters()) {
				convertType(paraType);
			}
		}
	}
	
	/**
	 * Check and convert a type
	 * 
	 * @param type, TypeInfo instance
	 */
	private void convertType(TypeInfo type) {
		String targetTypeFullName = typeMapping.get(type.getFullName());
		if (targetTypeFullName != null) {
			type.setFullName(targetTypeFullName);
			type.setName(ClassNameUtil.stripQualifier(targetTypeFullName));			
		}
	}
	
	
	// for java implementation, we need to change nested class into package-member class,
	// so package name should be adjusted accordingly
	private void adjustPackageNameOfNestClass(List<ClassInfo> classes) {
		for(ClassInfo classInfo : classes) {
			// adjust class
			if (classInfo.isNestClass()) {
				String pkgName = classInfo.getPackageName().toLowerCase();
				classInfo.setPackageName(pkgName);
				classInfo.setFullName(pkgName + "." + classInfo.getName());
			}
			// adjust fields
			for(FieldInfo fieldInfo : classInfo.getFields()) {
				TypeInfo attrType = fieldInfo.getType();
				
				// no need to handle primitive type
				if (attrType.isPrimitive())
					continue;
				
				// handle array
				if (attrType.isArray()) {
					// T of T[]
					TypeInfo elementType = attrType.getElementType();
					// no need to handle primitive type
					if (elementType.isPrimitive()) {
						continue;
					}
					if (elementType.isNestClass()) {
						String elementTypePackageName = ClassNameUtil
								.getPackageName(elementType.getFullName());
						elementTypePackageName = elementTypePackageName.toLowerCase();
						elementType.setFullName(elementTypePackageName + "." + elementType.getName());
					}
					continue;
				}
				
				if (attrType.isNestClass()) {
					String attrTypePackageName = ClassNameUtil.getPackageName(attrType.getFullName());
					attrTypePackageName = attrTypePackageName.toLowerCase();
					attrType.setFullName(attrTypePackageName + "." + attrType.getName());
				}
				
				// has type parameters?
				for (TypeInfo paramType : attrType.getTypeParameters()) {
					if (paramType.isNestClass()) {
						String paramTypePackageName = ClassNameUtil
								.getPackageName(paramType.getFullName());
						paramTypePackageName = paramTypePackageName.toLowerCase();
						paramType.setFullName(paramTypePackageName + "." + paramType.getName());
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * helper to find out all classes that should be imported by a class
	 * 
	 * @param clazz
	 *            , ClassInfo instance
	 * @return a set of class names that should be imported
	 */
	private Set<String> getClassImports(ClassInfo clazz) {
		Set<String> imports = new HashSet<String>();
		String clazzPackageName = clazz.getPackageName();

		// extends super class?
		if (clazz.getSuperClass() != null) {
			TypeInfo superClassType = clazz.getSuperClass();
			String superClassTypePackageName = ClassNameUtil
					.getPackageName(superClassType.getFullName());
			if (needImport(clazzPackageName, superClassTypePackageName)) {
				imports.add(ClassNameUtil.erase(superClassType.getFullName()));
			}

		}

		// import field type
		for (FieldInfo field : clazz.getFields()) {
			TypeInfo attrType = field.getType();
			String attrTypePackageName = ClassNameUtil.getPackageName(attrType.getFullName());

			// no import for primitive type
			if (attrType.isPrimitive())
				continue;

			// import component type of array
			if (attrType.isArray()) {
				// T of T[]
				TypeInfo elementType = attrType.getElementType();
				// no import for primitive type
				if (elementType.isPrimitive()) {
					continue;
				}
				String elementTypePackageName = ClassNameUtil
						.getPackageName(elementType.getFullName());
				if (needImport(clazzPackageName, elementTypePackageName)) {
					imports.add(ClassNameUtil.erase(elementType.getFullName()));
				}
				continue;
			}

			if (needImport(clazzPackageName, attrTypePackageName)) {
				// erase type parameters before import
				imports.add(ClassNameUtil.erase(attrType.getFullName()));
			}
			// has type parameters?
			for (TypeInfo paramType : attrType.getTypeParameters()) {
				String paramTypePackageName = ClassNameUtil
						.getPackageName(paramType.getFullName());
				if (needImport(clazzPackageName, paramTypePackageName)) {
					// erase type parameters before import
					imports.add(ClassNameUtil.erase(paramType.getFullName()));
				}
			}
		}

		return imports;		
	}
	
	private boolean needImport(String current, String target) {
		if (!target.equals(current) && !target.startsWith(JAVA_DEFAULT_PACKAGE_NAME)) {
			return true;
		}
		return false;
	}

	@Override
	protected URL getTemplateURL(String template) throws XjcModuleException {
		URL url = NanoClientModule.class.getResource(TEMPLATE_FOLDER + "/" + template);
		if (url == null) {
			throw new XjcModuleException("Fail to load required template file : "
					+ template);
		}
		debug("AndroidClientModule get template : " + url.toString());
		return url;
	}

}
