package com.leansoft.mxjc.writer;

import java.io.IOException;
import java.io.OutputStream;

import com.leansoft.mxjc.model.FileInfo;

/**
 * Code Writer Interface
 * 
 * @author bulldog
 *
 */
public interface ICodeWriter {
	
	
    /**
     * Called by code generator to store the specified file.
     * The callee must allocate a storage to store the specified file.
     * 
     * <p>
     * The returned stream will be closed before the next file is
     * stored. So the callee can assume that only one OutputStream
     * is active at any given time.
     * 
     * @param   file
     *      Value object holding information about a code generation target file
     */
	public OutputStream openStream(FileInfo file) throws IOException;
	
	

    /**
     * Called by code generator at the end of the process.
     */
    public void close() throws IOException;
}
