package com.leansoft.mxjc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Set;

import com.leansoft.mxjc.builder.ModelBuilder;
import com.leansoft.mxjc.model.CGConfig;
import com.leansoft.mxjc.model.CGModel;
import com.leansoft.mxjc.model.FileInfo;
import com.leansoft.mxjc.module.ClientModule;
import com.leansoft.mxjc.module.ModuleFactory;
import com.leansoft.mxjc.module.ModuleName;
import com.leansoft.mxjc.module.XjcModuleException;
import com.leansoft.mxjc.writer.FileCodeWriter;
import com.leansoft.mxjc.writer.ICodeWriter;
import com.leansoft.mxjc.writer.ZipCodeWriter;
import com.leansoft.mxjc.writer.ProgressCodeWriter;
import com.sun.codemodel.JCodeModel;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.sun.tools.xjc.generator.bean.BeanGenerator;
import com.sun.tools.xjc.model.Model;
import com.sun.tools.xjc.outline.Outline;
import com.sun.tools.xjc.reader.gbind.Expression;
import com.sun.tools.xjc.reader.gbind.Graph;
import com.sun.tools.xjc.reader.internalizer.DOMForest;
import com.sun.tools.xjc.reader.xmlschema.ExpressionBuilder;
import com.sun.tools.xjc.reader.xmlschema.parser.XMLSchemaInternalizationLogic;
import com.sun.tools.xjc.util.ErrorReceiverFilter;
import com.sun.tools.xjc.util.NullStream;
import com.sun.tools.xjc.util.Util;
import com.sun.tools.xjc.writer.SignatureWriter;
import com.sun.tools.xjc.AbortException;
import com.sun.tools.xjc.BadCommandLineException;
import com.sun.tools.xjc.ErrorReceiver;
import com.sun.tools.xjc.ModelLoader;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.XJCListener;
import com.sun.tools.xjc.ConsoleErrorReporter;
import com.sun.tools.xjc.Language;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSSchemaSet;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


/**
 * CUI of XJC.
 * 
 * @author bulldog, adapted from jaxb-xjc
 */
public class Driver {

    public static void main(final String[] args) throws Exception {
        // use the platform default proxy if available.
        // see sun.net.spi.DefaultProxySelector for details.
        try {
            System.setProperty("java.net.useSystemProxies","true");
        } catch (SecurityException e) {
            // failing to set this property isn't fatal
        }

        if( Util.getSystemProperty(Driver.class,"noThreadSwap")!=null )
            _main(args);    // for the ease of debugging

        // run all the work in another thread so that the -Xss option
        // will take effect when compiling a large schema. See
        // http://developer.java.sun.com/developer/bugParade/bugs/4362291.html
        final Throwable[] ex = new Throwable[1];

        Thread th = new Thread() {
            public void run() {
                try {
                    _main(args);
                } catch( Throwable e ) {
                    ex[0]=e;
                }
            }
        };
        th.start();
        th.join();

        if(ex[0]!=null) {
            // re-throw
            if( ex[0] instanceof Exception )
                throw (Exception)ex[0];
            else
                throw (Error)ex[0];
        }
    }

    private static void _main( String[] args ) throws Exception {
        try {
            System.exit(run( args, System.out, System.out ));
        } catch (BadCommandLineException e) {
            // there was an error in the command line.
            // print usage and abort.
            if(e.getMessage()!=null) {
                System.out.println(e.getMessage());
                System.out.println();
            }

            usage(e.getOptions(),false);
            System.exit(-1);
        }
    }



    /**
     * Performs schema compilation and prints the status/error into the
     * specified PrintStream.
     *
     * <p>
     * This method could be used to trigger XJC from other tools,
     * such as Ant or IDE.
     *
     * @param    args
     *      specified command line parameters. If there is an error
     *      in the parameters, {@link BadCommandLineException} will
     *      be thrown.
     * @param    status
     *      Status report of the compilation will be sent to this object.
     *      Useful to update users so that they will know something is happening.
     *      Only ignorable messages should be sent to this stream.
     *
     *      This parameter can be null to suppress messages.
     *
     * @param    out
     *      Various non-ignorable output (error messages, etc)
     *      will go to this stream.
     *
     * @return
     *      If the compiler runs successfully, this method returns 0.
     *      All non-zero values indicate an error. The error message
     *      will be sent to the specified PrintStream.
     */
    public static int run(String[] args, final PrintStream status, final PrintStream out)
        throws Exception {

        class Listener extends XJCListener {
            ConsoleErrorReporter cer = new ConsoleErrorReporter(out==null?new PrintStream(new NullStream()):out);

            public void generatedFile(String fileName, int count, int total) {
                message(fileName);
            }
            public void message(String msg) {
                if(status!=null)
                    status.println(msg);
            }

            public void error(SAXParseException exception) {
                cer.error(exception);
            }

            public void fatalError(SAXParseException exception) {
                cer.fatalError(exception);
            }

            public void warning(SAXParseException exception) {
                cer.warning(exception);
            }

            public void info(SAXParseException exception) {
                cer.info(exception);
            }
        }

        return run(args,new Listener());
    }

    /**
     * Performs schema compilation and prints the status/error into the
     * specified PrintStream.
     *
     * <p>
     * This method could be used to trigger XJC from other tools,
     * such as Ant or IDE.
     *
     * @param    args
     *        specified command line parameters. If there is an error
     *        in the parameters, {@link BadCommandLineException} will
     *        be thrown.
     * @param    listener
     *      Receives messages from XJC reporting progress/errors.
     *
     * @return
     *      If the compiler runs successfully, this method returns 0.
     *      All non-zero values indicate an error. The error message
     *      will be sent to the specified PrintStream.
     */
    public static int run(String[] args, @NotNull final XJCListener listener) throws BadCommandLineException {

        // recognize those special options before we start parsing options.
        for (String arg : args) {
            if (arg.equals("-version")) {
                listener.message(Messages.format(Messages.VERSION));
                return -1;
            }
        }

        final OptionsEx opt = new OptionsEx();
        opt.setSchemaLanguage(Language.XMLSCHEMA);  // disable auto-guessing
        try {
            opt.parseArguments(args);
        } catch (WeAreDone _) {
            return -1;
        } catch(BadCommandLineException e) {
            e.initOptions(opt);
            throw e;
        }

        // display a warning if the user specified the default package
        // this should work, but is generally a bad idea
        if(opt.defaultPackage != null && opt.defaultPackage.length()==0) {
            listener.message(Messages.format(Messages.WARNING_MSG, Messages.format(Messages.DEFAULT_PACKAGE_WARNING)));
        }


        // set up the context class loader so that the user-specified classes
        // can be loaded from there
        final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(
            opt.getUserClassLoader(contextClassLoader));

        // parse a grammar file
        //-----------------------------------------
        try {
            if( !opt.quiet ) {
                listener.message(Messages.format(Messages.PARSING_SCHEMA));
            }

            final boolean[] hadWarning = new boolean[1];

            ErrorReceiver receiver = new ErrorReceiverFilter(listener) {
                public void info(SAXParseException exception) {
                    if(opt.verbose)
                        super.info(exception);
                }
                public void warning(SAXParseException exception) {
                    hadWarning[0] = true;
                    if(!opt.quiet)
                        super.warning(exception);
                }
                @Override
                public void pollAbort() throws AbortException {
                    if(listener.isCanceled())
                        throw new AbortException();
                }
            };

            if( opt.mode==Mode.FOREST ) {
                // dump DOM forest and quit
                ModelLoader loader  = new ModelLoader( opt, new JCodeModel(), receiver );
                try {
                    DOMForest forest = loader.buildDOMForest(new XMLSchemaInternalizationLogic());
                    forest.dump(System.out);
                    return 0;
                } catch (SAXException e) {
                    // the error should have already been reported
                } catch (IOException e) {
                    receiver.error(e);
                }

                return -1;
            }

            if( opt.mode==Mode.GBIND ) {
                try {
                    XSSchemaSet xss = new ModelLoader(opt, new JCodeModel(), receiver).loadXMLSchema();
                    Iterator<XSComplexType> it = xss.iterateComplexTypes();
                    while (it.hasNext()) {
                        XSComplexType ct =  it.next();
                        XSParticle p = ct.getContentType().asParticle();
                        if(p==null)     continue;

                        Expression tree = ExpressionBuilder.createTree(p);
                        System.out.println("Graph for "+ct.getName());
                        System.out.println(tree.toString());
                        Graph g = new Graph(tree);
                        System.out.println(g.toString());
                        System.out.println();
                    }
                    return 0;
                } catch (SAXException e) {
                    // the error should have already been reported
                }
                return -1;
            }
            
            Model model = ModelLoader.load( opt, new JCodeModel(), receiver );

            if (model == null) {
                listener.message(Messages.format(Messages.PARSE_FAILED));
                return -1;
            }

            if( !opt.quiet ) {
                listener.message(Messages.format(Messages.COMPILING_SCHEMA));
            }

            switch (opt.mode) {
            case SIGNATURE :
                try {
                    SignatureWriter.write(
                        BeanGenerator.generate(model,receiver),
                        new OutputStreamWriter(System.out));
                    return 0;
                } catch (IOException e) {
                    receiver.error(e);
                    return -1;
                }

            case CODE :
            case DRYRUN :
            case ZIP :
                {
                	CGModel cgModel;
                    // generate actual code
                    receiver.debug("generating code ...");
                    {// don't want to hold outline in memory for too long.
                        Outline outline = model.generateCode(opt,receiver);
                        if(outline==null) {
                            listener.message(
                                Messages.format(Messages.FAILED_TO_GENERATE_CODE));
                            return -1;
                        }

                        listener.compiled(outline);
                        
                        // generate code generation model
                        cgModel = ModelBuilder.buildCodeGenModel(outline, receiver);
                        if(cgModel==null) {
                            listener.message(
                                Messages.format(Messages.FAILED_TO_BUILD_CODEGEN_MODEL));
                            return -1;
                        }
                    }

                    if( opt.mode == Mode.DRYRUN )
                        break;  // enough
                    

                    // use specific client module to generate code
                    Set<FileInfo> files;
                    try {
                        ClientModule clientModule = ModuleFactory.getModule(opt.module);
                        clientModule.setErrorReceiver(receiver);// enable reporting
                        clientModule.init();
                        
                        CGConfig cgConfig = new CGConfig();
                        cgConfig.picoPrefix = opt.prefix;
                        cgConfig.nanoPrivateField = opt.privateField;
						files = clientModule.generate(cgModel, cgConfig);
					} catch (XjcModuleException e1) {
						receiver.error(e1);
						return -1;
					}
                    
                    // then print them out
                    try {
                        ICodeWriter cw;
                        if( opt.mode==Mode.ZIP ) {
                            OutputStream os;
                            if(opt.targetDir.getPath().equals("."))
                                os = System.out;
                            else {
                            	File targetFile = new File(opt.targetDir, "gensrc.zip"); // TODO, make target file name configurable
                                os = new FileOutputStream(targetFile);
                            }


                            cw = new ZipCodeWriter(os);
                        } else
                            cw = new FileCodeWriter(opt.targetDir, opt.readOnly);

                        if( !opt.quiet ) {
                            cw = new ProgressCodeWriter(cw,listener, files.size());
                        }
                        CodeBuilder.build(files, cw);
                    } catch (IOException e) {
                    	//e.printStackTrace();
                        receiver.error(e);
                        return -1;
                    }

                    break;
                }
            default :
                assert false;
            }

            if(opt.debugMode) {
                try {
                    new FileOutputStream(new File(opt.targetDir,hadWarning[0]?"hadWarning":"noWarning")).close();
                } catch (IOException e) {
                    receiver.error(e);
                    return -1;
                }
            }

            return 0;
        } catch( StackOverflowError e ) {
            if(opt.verbose)
                // in the debug mode, propagate the error so that
                // the full stack trace will be dumped to the screen.
                throw e;
            else {
                // otherwise just print a suggested workaround and
                // quit without filling the user's screen
                listener.message(Messages.format(Messages.STACK_OVERFLOW));
                return -1;
            }
        }
    }

    public static String getBuildID() {
        return Messages.format(Messages.BUILD_ID);
    }


    /**
     * Operation mode.
     */
    private static enum Mode {
        // normal mode. compile the code
        CODE,

        // dump the signature of the generated code
        SIGNATURE,

        // dump DOMForest
        FOREST,

        // same as CODE but don't produce any Java source code
        DRYRUN,

        // same as CODE but pack all the outputs into a zip and dumps to stdout
        ZIP,

        // testing a new binding mode
        GBIND
    }

    
    /**
     * Command-line arguments processor.
     * 
     * <p>
     * This class contains options that only make sense
     * for the command line interface.
     */
    static class OptionsEx extends Options
    {
        /** Operation mode. */
        protected Mode mode = Mode.CODE;
        
        /** A switch that determines the behavior in the BGM mode. */
        public boolean noNS = false;
        
        public ModuleName module = ModuleName.NANO;
        
        public String prefix; // for pico
        
        public boolean privateField; // for nano
               
        /** Parse XJC-specific options. */
        public int parseArgument(String[] args, int i) throws BadCommandLineException {
            if (args[i].equals("-noNS")) {
                noNS = true;
                return 1;
            }
            if (args[i].equals("-mode")) {
                i++;
                if (i == args.length)
                    throw new BadCommandLineException(
                        Messages.format(Messages.MISSING_MODE_OPERAND));

                String mstr = args[i].toLowerCase();

                for( Mode m : Mode.values() ) {
                    if(m.name().toLowerCase().startsWith(mstr) && mstr.length()>2) {
                        mode = m;
                        return 2;
                    }
                }

                throw new BadCommandLineException(
                    Messages.format(Messages.UNRECOGNIZED_MODE, args[i]));
            }
            
            if (args[i].equals("-nano")) {
            	module = ModuleName.NANO;
            	return 1;
            }
            
            if (args[i].equals("-pico")) {
            	module = ModuleName.PICO;
            	return 1;
            }
            
            if (args[i].equals("-privateField")) {
            	this.privateField = true;
            	return 1;
            }
            
            if (args[i].equals("-prefix")) {
            	prefix = super.requireArgument("-prefix", args, ++i);
            	return 2;
            }
            
            if (args[i].equals("-help")) {
                usage(this,false);
                throw new WeAreDone();
            }
            if (args[i].equals("-private")) {
                usage(this,true);
                throw new WeAreDone();
            }

            return super.parseArgument(args, i);
        }
    }

    /**
     * Used to signal that we've finished processing.
     */
    private static final class WeAreDone extends BadCommandLineException {}


    /**
     * Prints the usage screen and exits the process.
     *
     * @param opts
     *      If the parsing of options have started, set a partly populated
     *      {@link Options} object.
     */
    public static void usage( @Nullable Options opts, boolean privateUsage ) {
        if( privateUsage ) {
            System.out.println(Messages.format(Messages.DRIVER_PRIVATE_USAGE));
        } else {
            System.out.println(Messages.format(Messages.DRIVER_PUBLIC_USAGE));
        }
        
        // do not show plugin usage
//        if( opts!=null && opts.getAllPlugins().size()!=0 ) {
//            System.out.println(Messages.format(Messages.ADDON_USAGE));
//            for (Plugin p : opts.getAllPlugins()) {
//                System.out.println(p.getUsage());
//            }
//        }
    }
}

