package com.leansoft.mxjc.writer;

import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.leansoft.mxjc.model.FileInfo;

/**
 * Writes all the files into a zip file.
 * 
 * @author boyang
 */
public class ZipCodeWriter implements ICodeWriter {
	
    
    private final ZipOutputStream zip;
    
    private final OutputStream filter;
    
    /**
     * @param target
     *      Zip file will be written to this stream.
     */
    public ZipCodeWriter( OutputStream target ) {
        zip = new ZipOutputStream(target);
        // nullify the close method.
        filter = new FilterOutputStream(zip){
            public void close() {}
        };
    }

	@Override
	public OutputStream openStream(FileInfo file) throws IOException {
        String name = file.getFullname();
        if(!file.isInDefaultPackage())    name = file.getPath() + File.separator +name;
        
        zip.putNextEntry(new ZipEntry(name));
        return filter;
	}

	@Override
    public void close() throws IOException {
        zip.close();
    }

}
