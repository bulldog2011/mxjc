package com.leansoft.mxjc.module;

import com.leansoft.mxjc.module.nano.NanoClientModule;
import com.leansoft.mxjc.module.pico.PicoClientModule;

public class ModuleFactory {
	
	public static ClientModule getModule(ModuleName name) {
		ClientModule module = new NanoClientModule();
		if (name == ModuleName.NANO) {
			module = new NanoClientModule();
		} else if (name == ModuleName.PICO) {
			module = new PicoClientModule();
		}else if (name == ModuleName.PICOARC) {
                        module = new PicoClientModule().setUseARC(true);
		}
		return module;
	}

}
