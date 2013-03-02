package com.leansoft.mxjc.module;

import java.util.Set;

import com.leansoft.mxjc.model.CGConfig;
import com.leansoft.mxjc.model.CGModel;
import com.leansoft.mxjc.model.FileInfo;
import com.sun.tools.xjc.ErrorReceiver;



/**
 * Interface for a client module.  A client module for a specific platform 
 * implements logic for code generation.
 *
 * @author bulldog
 */
public interface ClientModule {

	/**
	 * Get the name of the client module
	 * 
	 * @return The name of the client module
	 */
	public ModuleName getName();
	
	/**
	 * Set ErrorReceiver to be used for error reporting
	 * 
	 * @param errorReceiver
	 */
	public void setErrorReceiver(ErrorReceiver errorReceiver);
	
	/**
	 * Initialize the module
	 * 
	 */
	public void init() throws XjcModuleException;
	
	
	/**
	 * Generate target code according to platform specific logic
	 * 
	 * @param context, code generation model
	 * @param config, config for code generation
	 * @return a set of generated file model
	 * @throws XjcModuleException
	 */
	public Set<FileInfo> generate(CGModel cgModel, CGConfig config) throws XjcModuleException;
}
