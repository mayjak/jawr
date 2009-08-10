/**
 * Copyright 2009 Ibrahim Chaehoi
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

import javax.servlet.http.HttpServletResponse;

import net.jawr.web.exception.ResourceNotFoundException;
import net.jawr.web.resource.ImageResourcesHandler;
import net.jawr.web.resource.bundle.CheckSumUtils;
import net.jawr.web.resource.bundle.factory.util.PathNormalizer;

import org.apache.log4j.Logger;

/**
 * Utility class for image tags.
 * 
 * @author ibrahim Chaehoi
 */
public final class ImageTagUtils {

	/** The logger */
	private static Logger logger = Logger.getLogger(ImageTagUtils.class);
	
	/**
	 * Returns the image URL generated by Jawr from a source image path
	 * @param imgSrc the source image path
     * @param imgRsHandler the image resource handler
	 * @param response the response
	 * @return the image URL generated by Jawr from a source image path
	 */
	public static String getImageUrl(String imgSrc, ImageResourcesHandler imgRsHandler, HttpServletResponse response) {
		
		if(imgRsHandler == null){
			throw new IllegalStateException("You are using a Jawr image tag while the Jawr Image servlet has not been initialized. Initialization of Jawr Image servlet either failed or never occurred.");
		}
	
		String newUrl = (String) imgRsHandler.getCacheUrl(imgSrc);
		
        if(newUrl == null){
        	try {
				newUrl = CheckSumUtils.getCacheBustedUrl(imgSrc, imgRsHandler.getRsHandler(), imgRsHandler.getJawrConfig());
				imgRsHandler.addMapping(imgSrc, newUrl);
        	} catch (IOException e) {
	    		logger.info("Unable to create the checksum for the image '"+imgSrc+"' while generating image tag.");
			} catch (ResourceNotFoundException e) {
				logger.info("Unable to find the image '"+imgSrc+"' while generating image tag.");
			}
    	}
        
        if(newUrl == null){
        	newUrl = imgSrc;
        }
        
        String imageServletMapping = imgRsHandler.getJawrConfig().getServletMapping();
		if("".equals(imageServletMapping)){
			if(newUrl.startsWith("/")){
				newUrl = newUrl.substring(1);
			}
		}else{
			newUrl = PathNormalizer.joinDomainToPath(imageServletMapping, newUrl);
		}
        
		return response.encodeURL(newUrl);
	}
}