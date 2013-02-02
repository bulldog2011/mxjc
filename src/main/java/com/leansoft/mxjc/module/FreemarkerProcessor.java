package com.leansoft.mxjc.module;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;


import freemarker.cache.URLTemplateLoader;
import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * Freemarker template processor
 * 
 * @author bulldog
 *
 */
public class FreemarkerProcessor {
	
	// freemarker template configuration
	private Configuration configuration;
	
	// singleton instance
	private static FreemarkerProcessor instance = new FreemarkerProcessor();
	
	/**
	 * Singleton constructor
	 */
	private FreemarkerProcessor() {
		this.configuration = this.getConfiguration();
	}
	
	/**
	 * Get singleton processor instance
	 * 
	 * @return Processor instance
	 */
	public static FreemarkerProcessor getInstance() {
		return instance;
	}
	
	/**
	 * Processes the specified template with the given model
	 * @param templateURL The template URL>
	 * @param model       The root model.
	 * @param out         The output writer to process to
	 * 
	 * @throws XjcModuleException 
	 */
	public void processTemplate(URL templateURL, Object model, Writer out) throws XjcModuleException {
		Template template;
		try {
			template = this.configuration.getTemplate(templateURL.toString());
		} catch (IOException e) {
			throw new XjcModuleException("exception to get template : " + templateURL.toString(), e);
		}
		if (template == null) {
			throw new XjcModuleException("template not found : " + templateURL.toString());
		}
		try {
			template.process(model, out);
		} catch (Exception e) {
			throw new XjcModuleException("Excpetion to process template " + templateURL.toString(), e);
		}
	}
	
	/**
	 * Processes the specific template with the given model
	 * 
	 * @param templateURL The template URL>
	 * @param model       The root model.
	 * @param out         The output stream to process to
	 * 
	 * @throws XjcModuleException 
	 */
	public void processTemplate(URL templateURL, Object model, OutputStream out) throws XjcModuleException {
		try {
			this.processTemplate(templateURL, model, new OutputStreamWriter(out, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			throw new XjcModuleException("Exception to process template " + templateURL.toString(), e);
		}
	}
	  
	/**
	 * Get the freemarker configuration.
	 *
	 * @return the freemarker configuration.
	 */
	private Configuration getConfiguration() {
	    Configuration configuration = new Configuration();
	    configuration.setTemplateLoader(getTemplateLoader());
	    configuration.setTemplateExceptionHandler(getTemplateExceptionHandler());
	    configuration.setLocalizedLookup(false);
	    configuration.setDefaultEncoding("UTF-8");
	    return configuration;
	}

	  
	/**
	 * Get the template exception handler.  The default one prints the stack trace to <code>System.err</code>
	 * rather than the writer because often Freemarker is printing to temp files.
	 *
	 * @return The template exception handler.
	 */
	private TemplateExceptionHandler getTemplateExceptionHandler() {
	    return new TemplateExceptionHandler() {
	      public void handleTemplateException(TemplateException templateException, Environment environment, Writer writer) throws TemplateException {
	        templateException.printStackTrace(System.err);
	        throw templateException;
	      }
	    };
	}

	/**
	 * Get the template loader for the freemarker configuration.
	 *
	 * @return the template loader for the freemarker configuration.
	 */
	private URLTemplateLoader getTemplateLoader() {
	    return new URLTemplateLoader() {
	      protected URL getURL(String name) {
	        try {
	          return new URL(name);
	        }
	        catch (MalformedURLException e) {
	          return null;
	        }
	      }
	    };
	}
}
