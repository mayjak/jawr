/**
 * Copyright 2016 Ibrahim Chaehoi
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 * 
 * 	http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package net.jawr.web.resource.bundle.mappings;

import net.jawr.web.resource.bundle.JoinableResourceBundle;

/**
 * The file path mapping for a bundle
 * 
 * @author Ibrahim Chaehoi
 */
public class FilePathMapping extends PathMapping {

	/** The last modified date of the file */
	private long lastModified;

	/**
	 * Constructor
	 * 
	 * @param bundle
	 *            the bundle
	 * @param mapping
	 *            the mapping
	 * @param lastModified
	 *            the last modified date
	 */
	public FilePathMapping(JoinableResourceBundle bundle, String mapping, long lastModified) {
		super(bundle, mapping);

		this.lastModified = lastModified;
	}

	/**
	 * @return the lastModified
	 */
	public long getLastModified() {
		return lastModified;
	}

	/**
	 * @param lastModified
	 *            the lastModified to set
	 */
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (lastModified ^ (lastModified >>> 32));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FilePathMapping other = (FilePathMapping) obj;
		if (lastModified != other.lastModified)
			return false;
		return true;
	}

}
