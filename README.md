mxjc
====

xsd to android java binding compiler based on jaxb xjc.


###Feature Highlight
1. ***Standard based*** : based on Oracle JAXB 2.1 XJC, recognize most standard XML Schema components.
2. ***Android Nano Binding Support*** : auto-generate Nano for Android bindable classes from XML Schema or WSDL.
3. ***Objective-c Pico Binding Support*** : ongoing.
3. ***Doc Auto Generation*** : auto-generate code comments from XML Schema or WSDL annotations.


###How to Use

download latest zip package [here](https://github.com/bulldog2011/bulldog-repo/tree/master/repo/releases/com/leansoft/mxjc/0.5.1), then extract the zip file.

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

###Docs
1. [Scheam driven data binding with Nano and mxjc](http://bulldog2011.github.com/blog/2013/02/07/schema-driven-nano-binding/)
2. [Schema Driven Web Serivce Client Development on Android, Part 1 : Hello eBay Finding](http://bulldog2011.github.com/blog/2013/02/17/schema-driven-on-android-part-1-hello-ebay-finding/)
3. [Schema Driven Web Serivce Client Development on Android, Part 2 : eBay Search App](http://bulldog2011.github.com/blog/2013/02/19/schema-driven-on-android-part-2-ebay-search/)


###Current Limition
1. xsd:any is not supported and will be ignored if presents
2. xsd:choice is not support and will be ignored if presents

###Copyright and License
Copyright 2012 LeanSoft, Inc.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License. You may obtain a copy of the License in the LICENSE file, or at:

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.



