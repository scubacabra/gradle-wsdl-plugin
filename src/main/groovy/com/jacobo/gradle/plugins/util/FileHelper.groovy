package com.jacobo.gradle.plugins.util

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

/**
 * File Helper utility class
 * User: djmijares
 * Date: 5/22/13
 * Time: 10:14 AM
 */
class FileHelper {

    private static final Logger log = Logging.getLogger(FileHelper.class)  

    /**
     * Figure out the ABSOLUTE schema location of the String relative to the parent/current directory
     * @param schemaLocation is the relative path of the schema location being called in either the xsd:import or xsd:includes call
     * @param parentDir is the parent directory of the schema file that is currently being Xml Slurped
     * @return File absolute File path to schema Location
     */
    public static File getAbsoluteSchemaLocation(String schemaLocation, File parentDir) {
        def relPath = new File(parentDir, schemaLocation)
	log.debug("Resolving Absolute Document Location -- Relative Path is '{}',  Full path is '{}'", schemaLocation, new File(relPath.canonicalPath).path)
        return new File(relPath.canonicalPath)
    }
}
