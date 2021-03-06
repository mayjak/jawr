/**
 * Copyright 2009-2014 Ibrahim Chaehoi
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
package net.jawr.web.taglib;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.jawr.web.JawrConstant;
import net.jawr.web.context.ThreadLocalJawrContext;
import net.jawr.web.resource.BinaryResourcesHandler;

/**
 * This class defines the tag which generate the URL used by Jawr to reference
 * an image resource.
 * 
 * @author Ibrahim Chaehoi
 * 
 */
public class ImagePathTag extends TagSupport {

	/** The serial version UID */
	private static final long serialVersionUID = 5066015622270484465L;

	/**
	 * The image source URI.
	 */
	protected String src = null;

	/**
	 * Variable name used to to store the path as a pageContext attribute
	 */
	private String var;

	/**
	 * The flag indicating if the image should be encoded in base 64
	 */
	private boolean base64;

	/**
	 * @return the var
	 */
	public String getVar() {
		return var;
	}

	/**
	 * @param var
	 *            the var to set
	 */
	public void setVar(String var) {
		this.var = var;
	}

	/**
	 * Returns the src attribute.
	 * 
	 * @return the src
	 */
	protected String getSrc() {
		return src;
	}

	/**
	 * Sets the src attribute as the image URL generated by Jawr for an image
	 * 
	 * @param imgSrc
	 *            the image path
	 * @param results
	 *            the result
	 * @return the image URL generated by Jawr for an image
	 * @throws JspException
	 *             if an exception occurs
	 */
	public void setSrc(String imgSrc) throws JspException {

		this.src = imgSrc;
	}

	/**
	 * Returns the flag indicating if the image should be encoded in base 64
	 * 
	 * @return the flag value to return
	 */
	public boolean getBase64() {
		return base64;
	}

	/**
	 * Returns the flag indicating if the image should be encoded in base 64
	 * 
	 * @param base64
	 *            the base64 to set
	 */
	public void setBase64(boolean base64) {
		this.base64 = base64;
	}

	/**
	 * Returns the flag indicating if the image should be encoded in base 64
	 * 
	 * @param base64
	 *            the base64 to set
	 */
	public void setBase64(String base64) {
		this.base64 = Boolean.valueOf(base64).booleanValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	public int doEndTag() throws JspException {

		try {
			String imgSrc = getImgSrcToRender();

			if (null == var) {
				try {
					pageContext.getOut().print(imgSrc);
				} catch (IOException e) {
					throw new JspException(e);
				}
			} else {
				pageContext.setAttribute(var, imgSrc);
			}

			return super.doEndTag();
			
		} finally {

			// Reset the Thread local for the Jawr context
			ThreadLocalJawrContext.reset();
		}
	}

	/**
	 * Returns the image source to render
	 * 
	 * @return the image source to render
	 * @throws JspException
	 *             if a JSP exception occurs.
	 */
	protected String getImgSrcToRender() throws JspException {

		BinaryResourcesHandler binaryRsHandler = (BinaryResourcesHandler) pageContext
				.getServletContext().getAttribute(
						JawrConstant.BINARY_CONTEXT_ATTRIBUTE);
		if (null == binaryRsHandler)
			throw new JspException(
					"You are using a Jawr image tag while the Jawr Binary servlet has not been initialized. Initialization of Jawr Image servlet either failed or never occurred.");

		HttpServletResponse response = (HttpServletResponse) pageContext
				.getResponse();

		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();

		return ImageTagUtils.getImageUrl(src, base64, binaryRsHandler, request,
				response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#release()
	 */
	public void release() {
		super.release();
		this.src = null;
	}
}
