/**
 * Copyright 2010-2012 Ibrahim Chaehoi
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
package net.jawr.web.resource.bundle.css;

import net.jawr.web.JawrConstant;
import net.jawr.web.config.JawrConfig;
import net.jawr.web.exception.ResourceNotFoundException;
import net.jawr.web.resource.BinaryResourcesHandler;
import net.jawr.web.resource.FileNameUtils;
import net.jawr.web.resource.bundle.CheckSumUtils;
import net.jawr.web.resource.bundle.factory.util.PathNormalizer;
import net.jawr.web.resource.bundle.generator.GeneratorRegistry;
import net.jawr.web.servlet.util.MIMETypesSupport;
import net.jawr.web.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * This class is used to rewrite CSS URLs according to the new
 * relative locations of the references, from the original CSS path to a new one. 
 * Since the path changes, the URLs must be rewritten accordingly. URLs in css files are
 * expected to be according to the css spec (see
 * http://www.w3.org/TR/REC-CSS2/syndata.html#value-def-uri). Thus, single
 * double, or no quotes enclosing the url are allowed (and remain as they are
 * after rewriting). Escaped parens and quotes are allowed within the url.
 * 
 * @author Ibrahim Chaehoi
 */
public class LessDebugImageUrlRewriter extends CssImageUrlRewriter {
	private static Logger LOGGER = LoggerFactory
			.getLogger(LessDebugImageUrlRewriter.class);
	private JawrConfig jawrConfig;

	public LessDebugImageUrlRewriter(JawrConfig config) {
		super(config);
		this.jawrConfig = config;
	}
	protected String getRewrittenImagePath(String originalCssPath,
										   String newCssPath, String url) throws IOException {

		BinaryResourcesHandler binaryRsHandler = (BinaryResourcesHandler) jawrConfig
				.getContext().getAttribute(JawrConstant.BINARY_CONTEXT_ATTRIBUTE);
		String binaryServletPath = "";

		if (binaryRsHandler != null) {
			binaryServletPath = PathNormalizer.asPath(binaryRsHandler.getConfig()
					.getServletMapping());
		}

		String imgUrl = null;

		// Retrieve the current CSS file from which the CSS image is referenced
		String currentCss = originalCssPath;
		boolean generatedImg = false;
		if (binaryRsHandler != null) {
			GeneratorRegistry imgRsGeneratorRegistry = binaryRsHandler.getConfig()
					.getGeneratorRegistry();
			generatedImg = imgRsGeneratorRegistry.isGeneratedBinaryResource(url);
		}

		boolean cssGeneratorIsHandleCssImage = isCssGeneratorHandlingCssImage(currentCss);

		String rootPath = currentCss;

		// If the CSS image is taken from the classpath, add the classpath cache
		// prefix
		if (generatedImg || cssGeneratorIsHandleCssImage) {

			String tempUrl = url;

			// If it's a classpath CSS, the url of the CSS image is defined
			// relatively to it.
			if (cssGeneratorIsHandleCssImage && !generatedImg) {
				tempUrl = PathNormalizer.concatWebPath(rootPath, url);
			}

			// generate image cache URL
			imgUrl = rewriteURL(tempUrl, binaryServletPath, newCssPath,
					binaryRsHandler);
		} else {

			if (jawrConfig.getGeneratorRegistry().isPathGenerated(rootPath)) {
				rootPath = rootPath.substring(rootPath
						.indexOf(GeneratorRegistry.PREFIX_SEPARATOR) + 1);
			}

			// Generate the image URL from the current CSS path
			imgUrl = PathNormalizer.concatWebPath(rootPath, url);
			imgUrl = rewriteURL(imgUrl, binaryServletPath, newCssPath,
					binaryRsHandler);
		}

		// This following condition should never be true.
		// If it does, it means that the image path is wrongly defined.
		if (imgUrl == null) {
			LOGGER.error("The CSS image path for '"
					+ url
					+ "' defined in '"
					+ currentCss
					+ "' is out of the application context. Please check your CSS file.");
		}

		return imgUrl;
	}

	private boolean isCssGeneratorHandlingCssImage(String currentCss) {
		return jawrConfig.getGeneratorRegistry()
				.isHandlingCssImage(currentCss);
	}


	protected String rewriteURL(String url,
								String binaryServletPath, String newCssPath,
								BinaryResourcesHandler binaryRsHandler) throws IOException {

		String imgUrl = url;
		if (isBinaryResource(imgUrl)) {
			imgUrl = addCacheBuster(url, binaryRsHandler);
			// Add image servlet path in the URL, if it's defined
			if (StringUtils.isNotEmpty(binaryServletPath)) {
				imgUrl = binaryServletPath + JawrConstant.URL_SEPARATOR + imgUrl;
			}
		}

		imgUrl = PathNormalizer.asPath(imgUrl);
		return PathNormalizer.getRelativeWebPath(
				PathNormalizer.getParentPath(newCssPath), imgUrl);
	}

	/**
	 * Checks if the resource is an binary resource
	 *
	 * @param resourcePath
	 *            the resourcePath
	 * @return true if the resource is an binary resource
	 */
	protected boolean isBinaryResource(String resourcePath) {
		String extension = FileNameUtils.getExtension(resourcePath);
		if (extension != null) {
			extension = extension.toLowerCase();
		}
		return MIMETypesSupport.getSupportedProperties(this).containsKey(
				extension);
	}

	/**
	 * Adds the cache buster to the CSS image
	 *
	 * @param url
	 *            the URL of the image
	 * @param binaryRsHandler
	 *            the binary resource handler
	 * @return the url of the CSS image with a cache buster
	 * @throws IOException
	 *             if an IO exception occurs
	 */
	@SuppressWarnings("unchecked")
	private String addCacheBuster(String url,
								  BinaryResourcesHandler binaryRsHandler) throws IOException {

		// Try to retrieve the cache busted URL from the bundle processing cache
		String newUrl = null;
		// Try to retrieve the from the image resource handler cache
		if (binaryRsHandler != null) {
			newUrl = binaryRsHandler.getCacheUrl(url);
			if (newUrl != null) {
				return newUrl;
			}
			// Retrieve the new URL with the cache prefix
			try {
				newUrl = CheckSumUtils.getCacheBustedUrl(url,
						binaryRsHandler.getRsReaderHandler(),
						binaryRsHandler.getConfig());
			} catch (ResourceNotFoundException e) {
				LOGGER.info("Impossible to define the checksum for the resource '"
						+ url + "'. ");
				return url;
			} catch (IOException e) {
				LOGGER.info("Impossible to define the checksum for the resource '"
						+ url + "'.");
				return url;
			}

			binaryRsHandler.addMapping(url, newUrl);

		} else {
			newUrl = url;
		}

		return newUrl;
	}

}
