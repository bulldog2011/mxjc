package com.leansoft.mxjc.module;

import com.leansoft.mxjc.module.nano.NanoClientModule;

public class ModuleFactory {
	
	public static ClientModule getModule(ModuleName name) {
		ClientModule module = new NanoClientModule();
		if (name == ModuleName.NANO) {
			module = new NanoClientModule();
		}
		return module;
	}

}
