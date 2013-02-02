package com.leansoft.mxjc.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import com.leansoft.mxjc.model.FileInfo;


/**
 * Writes all the source files under the specified file folder.
 * 
 * @author bulldog
 */
public class FileCodeWriter implements ICodeWriter {
	
    /** The target directory to put source code. */
    private final File target;
    
    /** specify whether or not to mark the generated files read-only */
    private final boolean readOnly;

    /** Files that shall be marked as read only. */
    private final Set<File> readonlyFiles = new HashSet<File>();
    
    public FileCodeWriter(File target ) throws IOException {
        this(target,false);
    }
    
    public FileCodeWriter(File target, boolean readOnly ) throws IOException {
        this.target = target;
        this.readOnly = readOnly;
        if(!target.exists() || !target.isDirectory())
            throw new IOException(target + ": non-existent directory");
    }

	@Override
	public OutputStream openStream(FileInfo fileInfo) throws IOException {
        return new FileOutputStream(getFile(fileInfo));
	}

	@Override
	public void close() throws IOException {
        // mark files as read-only if necessary
        for (File f : readonlyFiles)
            f.setReadOnly();
	}
	
    private File getFile(FileInfo fileInfo) throws IOException {
        File dir;
        if(fileInfo.isInDefaultPackage()) // the file resides in default package
            dir = target;
        else
            dir = new File(target, fileInfo.getPath());
        
        if(!dir.exists())   dir.mkdirs();
        
        File fn = new File(dir,fileInfo.getFullname());
        
        if (fn.exists()) {
            if (!fn.delete())
                throw new IOException(fn + ": Can't delete previous version");
        }
        
        if(readOnly)        readonlyFiles.add(fn);
        return fn;
    	
    }

}
