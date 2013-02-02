package com.leansoft.mxjc.model.nano;

import com.leansoft.mxjc.model.Annotatable;
import com.leansoft.mxjc.util.StringUtil;

public class AttributeAnnotation implements Annotatable {

	private String name = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean isParameterProvided() {
		return !StringUtil.isEmpty(name);
	}

	
	public String toString() {
		String value = "";
		if (!StringUtil.isEmpty(name)) {
			value += "name = \"" + name + "\"";;
		}
		value = "Attribute(" + value + ")";
		return value;
	}
	
}
