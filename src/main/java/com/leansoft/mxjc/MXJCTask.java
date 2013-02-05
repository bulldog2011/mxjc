package com.leansoft.mxjc;

import java.io.IOException;

import com.sun.istack.tools.ProtectedTask;
import com.sun.tools.xjc.XJCTask;

public class MXJCTask extends ProtectedTask {
	
    private String source = "2.0";

	@Override
	protected String getCoreClassName() {
		return "com.leansoft.mxjc.MXJC2Task";
	}

	@Override
	protected ClassLoader createClassLoader() throws ClassNotFoundException,
			IOException {
        return ClassLoaderBuilder.createProtectiveClassLoader(XJCTask.class.getClassLoader(), source);
	}

}
