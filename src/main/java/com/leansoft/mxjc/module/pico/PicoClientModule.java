package com.leansoft.mxjc.module.pico;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import com.leansoft.mxjc.model.CGConfig;
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

public class PicoClientModule extends AbstractClientModule {
	
	// references to templates
	private URL clzIntfTemplate;
	private URL clzImplTempalte;
	private URL enumDeclarationTemplate;
	private URL enumDefinitionTemplate;
	private URL commonHeaderTemplate;
	
	@Override
	public ModuleName getName() {
		return ModuleName.PICO;
	}

	@Override
	public void init() throws XjcModuleException {
		info("PicoClientModule loading templates ...");
		loadTemplates();
	}
	
	private void loadTemplates() throws XjcModuleException {
		//load template
		clzIntfTemplate = this.getTemplateURL("client-class-interface.fmt");
		clzImplTempalte = this.getTemplateURL("client-class-implementation.fmt");
		enumDeclarationTemplate = this.getTemplateURL("client-enum-declaration.fmt");
		enumDefinitionTemplate = this.getTemplateURL("client-enum-definition.fmt");
		commonHeaderTemplate = this.getTemplateURL("client-common-header.fmt");
	}

	@Override
	public Set<FileInfo> generate(CGModel cgModel, CGConfig config)
			throws XjcModuleException {
		// freemarker datamodel
		SimpleHash fmModel = this.getFreemarkerModel();
		
		// container for target codes
		Set<FileInfo> targetFileSet = new HashSet<FileInfo>();
		
		info("Generating the Pico client classes...");
		
		if (config.picoPrefix == null) {
			warn("No prefix is provided, it's recommended to add prefix for Pico binding to avoid possible conflict");
		}
		String prefix = config.picoPrefix == null ? "" : config.picoPrefix;
		prefixType(cgModel, prefix);
		
		fmModel.put("group", config.picoServiceGroup);
		
		
		String relativePath = null;
		// generate classes
		info("Generating classes ...");
		for(ClassInfo classInfo : cgModel.getClasses()) {
			this.convertFieldsType(classInfo);
			fmModel.put("superClassImports", this.getSuperClassImports(classInfo));
			fmModel.put("fieldClassImports", this.getFieldImports(classInfo));
			fmModel.put("clazz", classInfo);
			
			relativePath = ClassNameUtil.packageNameToPath(classInfo.getPackageName());
			FileInfo classIntf = this.generateFile(clzIntfTemplate, fmModel, classInfo.getName(), "h", relativePath);
			targetFileSet.add(classIntf);
			FileInfo classImpl = this.generateFile(clzImplTempalte, fmModel, classInfo.getName(), "m", relativePath);
			targetFileSet.add(classImpl);
		}
		
		// generate enums
		info("Generating enums ...");
		for(EnumInfo enumInfo : cgModel.getEnums()) {
			fmModel.put("enum", enumInfo);
			relativePath = ClassNameUtil.packageNameToPath(enumInfo.getPackageName());
			FileInfo enumDec = this.generateFile(enumDeclarationTemplate, fmModel, enumInfo.getName(), "h", relativePath);
			targetFileSet.add(enumDec);
			FileInfo enumDef = this.generateFile(enumDefinitionTemplate, fmModel, enumInfo.getName(), "m", relativePath);
			targetFileSet.add(enumDef);
		}
		
		// generate common header
		info("Generating common header ...");
		fmModel.put("classes", cgModel.getClasses());
		fmModel.put("enums", cgModel.getEnums());
		
		if (relativePath == null) {
			relativePath = "";
		}
		relativePath += File.separator + "common";
		String commonTypeFileName = prefix + "CommonTypes";
		FileInfo commonHeader = this.generateFile(commonHeaderTemplate, fmModel, commonTypeFileName, "h", relativePath);
		targetFileSet.add(commonHeader);
		
		return targetFileSet;
	}
	
	// add prefix to avoid possible conflict
	private void prefixType(CGModel model, String prefix) {
		// add prefix on class
		for(ClassInfo classInfo : model.getClasses()) {
			classInfo.setName(prefix + classInfo.getName());
			String newFullName = classInfo.getPackageName() + "." + classInfo.getName();
			classInfo.setFullName(newFullName);
		}
		
		// add prefix on enum
		for(EnumInfo enumInfo : model.getEnums()) {
			enumInfo.setName(prefix + enumInfo.getName());
			String newFullName = enumInfo.getPackageName() + "." + enumInfo.getName();
			enumInfo.setFullName(newFullName);
		}
		
		for(ClassInfo classInfo : model.getClasses()) {
			// update super class reference
			if (classInfo.getSuperClass() != null) {
				TypeInfo superType = classInfo.getSuperClass();
				this.prefixType(superType, prefix);
			}
			
			// update field reference
			for(FieldInfo field : classInfo.getFields()) {
				TypeInfo fieldType = field.getType();
				this.prefixType(fieldType, prefix);
				
				if (fieldType.isArray()) {
					this.prefixType(fieldType.getElementType(), prefix);
				}
				
				// convert type parameters
				for(TypeInfo paraType : fieldType.getTypeParameters()) {
					this.prefixType(paraType, prefix);
				}
			}
		}
	}
	
	
	// add prefix in the type full name
	private void prefixType(TypeInfo type, String prefix) {
		if (type == null) return; // be cautious
		// for pico primitives, do not prefix
		if (Java2PicoTypeMapper.lookupPicoType(type.getFullName()) != null) {
			return;
		}
		String name = type.getName();
		type.setName(prefix + name);
		type.setFullName(prefix + name); // remove package for pico
	}
	
	private Set<String> getSuperClassImports(ClassInfo clazz) {
		Set<String> imports = new HashSet<String>();
		
		// extends super class?
		if (clazz.getSuperClass() != null) {
			TypeInfo superClassType = clazz.getSuperClass();
			imports.add(superClassType.getFullName());
		}
		
		return imports;
	}
	
	private Set<String> getFieldImports(ClassInfo clazz) {
		Set<String> imports = new HashSet<String>();
		
		for(FieldInfo field : clazz.getFields()) {
			TypeInfo fieldType = field.getType();
			if (fieldType.isArray()) {
				TypeInfo elementType = fieldType.getElementType();
				if (elementType != null && !elementType.isPrimitive() && !elementType.isEnum()) {
					imports.add(elementType.getFullName());
				}
			} else {
				if (!fieldType.isPrimitive() && !fieldType.isEnum() && !fieldType.isCollection()) {
					imports.add(fieldType.getFullName());
				}
			}
			// import type parameters
			for(TypeInfo paraType : fieldType.getTypeParameters()) { // object type
				if (!paraType.isPrimitive() && !paraType.isEnum() && !field.isAny()) {
					imports.add(paraType.getFullName());
				}
			}
		}
		
		return imports;
	}
	
	private void convertFieldsType(ClassInfo clazz) {
		for (FieldInfo field : clazz.getFields()) {
			TypeInfo fieldType = field.getType();
			convertType(fieldType);
			// TODO should element type of array be converted?
//			if (fieldType.isArray()) { 
//				convertType(fieldType.getElementType());
//			}
			// convert type parameters
			for(TypeInfo paraType : fieldType.getTypeParameters()) {
				convertType(paraType);
			}
		}
	}
	
	/**
	 * Check and covert a type
	 * 
	 * @param type
	 */
	private void convertType(TypeInfo type) {
		if (type == null) return; // be cautious
		String picoPrimitiveType = Java2PicoTypeMapper.lookupPicoType(type.getFullName());
		if (picoPrimitiveType != null)  {// pico primitive type
			type.setFullName(PicoTypeMapper.lookupWrapper(picoPrimitiveType));
			type.setName(picoPrimitiveType); // pico primitive
			type.setPrimitive(true);
		} else if (type.isEnum()) {
			type.setName(PicoType.ENUM); // pico enum type
			type.setPrimitive(true); // treat enum as primitive type
		} else {
			type.setName(PicoType.OBJECT);
			type.setPrimitive(false);
		}
	}

	@Override
	protected URL getTemplateURL(String template) throws XjcModuleException {
		URL url = PicoClientModule.class.getResource("template/" + template);
		if (url == null) {
			throw new XjcModuleException("Fail to load required template file : "
					+ template);
		}
		debug("PicoClientModule get template : " + url.toString());
		return url;
	}


}
