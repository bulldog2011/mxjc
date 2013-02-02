package com.leansoft.mxjc.model;

/**
 * POJO holding information about a code generation target file
 * 
 * @author bulldog
 * 
 */
public class FileInfo {

	// name of the file
	private String name;

	// suffix of the file name
	private String suffix;

	// relative path of the file
	private String path;

	// content of the file
	private byte[] content;

	public String getName() {
		return name;
	}

	/**
	 * Set the name of the file
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getSuffix() {
		return suffix;
	}

	/**
	 * Set the suffix of the file
	 * 
	 * @param suffix
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * The relative path of this file, derived from package name,
	 * 
	 * may return null if the file resides in default pacakge.
	 * 
	 * @return
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Set the relative path of the file
	 * 
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	public byte[] getContent() {
		return content;
	}
	
	/**
	 * Set the content of the file
	 * 
	 * @param content
	 */
	public void setContent(byte[] content) {
		this.content = content;
	}
	
	/**
	 * full name of the file(name.suffix)
	 * @return full name
	 */
	public String getFullname() {
		return name + "." + suffix;
	}
	
	/**
	 * Does this file resides in the default package
	 * 
	 * @return true if this file resides in the default package, false otherwise
	 */
	public boolean isInDefaultPackage() {
		if (path == null || path.trim().length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + ((suffix == null) ? 0 : suffix.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileInfo other = (FileInfo) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		if (suffix == null) {
			if (other.suffix != null)
				return false;
		} else if (!suffix.equals(other.suffix))
			return false;
		return true;
	}
}
