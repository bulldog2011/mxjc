MXJC
====

XSD to IOS Objective-C and Android Java binding compiler based on JAXB XJC.


###Feature Highlight
1. ***Standard based*** : based on Oracle JAXB 2.1 XJC, recognize most standard XML Schema components.
2. ***Objective-c Pico Binding Support*** : auto-generate Pico for IOS bindable classes from XML Schema or WSDL.
3. ***Android Nano Binding Support*** : auto-generate [Nano for Android](https://github.com/bulldog2011/nano) bindable classes from XML Schema or WSDL.
4. ***Doc Auto Generation*** : auto-generate code comments from XML Schema or WSDL annotations.


###How to Use

Download latest zip package [here](https://github.com/bulldog2011/bulldog-repo/tree/master/repo/releases/com/leansoft/mxjc/0.5.1), then extract the zip file.

```
Usage: mxjc [-options ...] <schema file/URL/dir> ... [-b <bindinfo>] ...
If dir is specified, all schema files in it will be compiled.
Options:
  -nano              :  target Nano for Android as code generation target (default)
  -pico              :  target Pico for IOS as code generation target
  -prefix <prefix>   :  add prefix to the target classes, only for pico binding (recommended to avoid 
                        possible name conflict)
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
```

###Docs
1. [Scheam driven data binding with Nano and mxjc](http://bulldog2011.github.com/blog/2013/02/07/schema-driven-nano-binding/)
2. [Schema Driven Web Serivce Client Development on Android, Part 1 : Hello eBay Finding](http://bulldog2011.github.com/blog/2013/02/17/schema-driven-on-android-part-1-hello-ebay-finding/)
3. [Schema Driven Web Serivce Client Development on Android, Part 2 : eBay Search App](http://bulldog2011.github.com/blog/2013/02/19/schema-driven-on-android-part-2-ebay-search/)


###Current Limition
1. xsd:choice is not support and will be ignored if presents
2. xsd nest anonymous type is only experimentally supported and is not recommended to use.

###Copyright and License
(The MIT License)

Copyright (c) 2013 Leansoft Technology <51startup@sina.com>

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the 'Software'), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 


