package com.leansoft.mxjc.writer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import com.leansoft.mxjc.model.FileInfo;
import com.sun.tools.xjc.AbortException;
import com.sun.tools.xjc.XJCListener;

/**
 * {@link ICodeWriter} that reports progress to {@link XJCListener}.
 */
public class ProgressCodeWriter implements ICodeWriter {
	
    private int current;
    private final int totalFileCount;
    private ICodeWriter output;

    public ProgressCodeWriter( ICodeWriter output, XJCListener progress, int totalFileCount ) {
        this.progress = progress;
        this.totalFileCount = totalFileCount;
        this.output = output;
        if(progress==null)
            throw new IllegalArgumentException();
    }

    private final XJCListener progress;

	@Override
	public OutputStream openStream(FileInfo file) throws IOException {
		report(file);
		return this.output.openStream(file);
	}

	@Override
	public void close() throws IOException {
		this.output.close();
	}

	
    private void report(FileInfo file) {
        String name = file.getFullname();
        if(!file.isInDefaultPackage())    name = file.getPath() + File.separator +name;

        if(progress.isCanceled())
            throw new AbortException();
        progress.generatedFile(name,current++,totalFileCount);
    }
}
