mxjc
====

xsd to android java compiler based on jaxb xjc.


###Feature Highlight
1. ***Standard & Mature*** : based on Oracle JAXB 2.1 XJC, recognize most standard Xml schema or WSDL components.
2. ***Nano Binding Support*** : auto-generate Nano for Android bindable classes from Xml schema or WSDL.
3. ***Doc Auto Generation*** : auto-generate code documents from Xml schema or WSDL.


###How to Use

download latest release [here](https://github.com/bulldog2011/bulldog-repo/tree/master/repo/releases/com/leansoft/mxjc/0.5.0)

	Usage: mxjc [-options ...] <schema file/URL/dir> ... [-b <bindinfo>] ...  
	If dir is specified, all schema files in it will be compiled.  
	Options:  
	    -nv                :  do not perform strict validation of the input schema(s)  
	    -b <file/dir>      :  specify external bindings files (each <file> must have its own -b)  
	                                               If a directory is given, **/*.xjb is searched  
	    -d <dir>           :  generated files will go into this directory  
	    -p <pkg>           :  specifies the target package  
	    -httpproxy <proxy> :  set HTTP/HTTPS proxy. Format is [user[:password]@]proxyHost:proxyPort  
	    -httpproxyfile <f> :  Works like -httpproxy but takes the argument in a file to protect password  
	
	    -readOnly          :  generated files will be in read-only mode  
	    -xmlschema         :  treat input as W3C XML Schema (default)  
	    -wsdl              :  treat input as WSDL and compile schemas inside it (experimental,unsupported)  
	    -verbose           :  be extra verbose  
	    -quiet             :  suppress compiler output  
	    -help              :  display this help message  
	    -version           :  display version information  