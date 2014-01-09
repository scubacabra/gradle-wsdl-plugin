package com.jacobo.gradle.plugins.model

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

/**
 * Implementation of DocumentSlurper for  XSD processing logic
 * @author jacobono
 */
class XsdSlurper extends DocumentSlurper {
    private static final Logger log = Logging.getLogger(XsdSlurper.class)

    /**
     * xsd import locations (relative to #documentFile currentDir)
     */
    def xsdImports = [] as Set

    /**
     * xsd includes locations (relative to #documentFile currentDir)
     */
    def xsdIncludes = [] as Set

    /**
     * grabs the dependencies of this Objects slurped document
     * slurps import statements and include statements
     * return none
     */
    @Override
    public void resolveDocumentDependencies() {
        log.debug("Getting document dependencies for xsd '{}'", this.documentFile.name)
        slurpDependencies(this.slurpedDocument?.import, this.xsdImports) // imports slurping
        slurpDependencies(this.slurpedDocument?.include, this.xsdIncludes) // includes slurping
	this.resolveRelativePathDependencies([this.xsdImports, this.xsdIncludes])
    }
    
    /**
     * dependentElements is the slurped elements needed, contains schemaLocation for either import/include data
     * collection is the collection to put the paths in -- all of are Strings (that's what schema Location is)
     **/
    def slurpDependencies(dependentElements, elementCollection) { 
      log.debug("Slurping Dependencies for '{}' elements of the '{}' type", dependentElements.size, dependentElements[0].name())
      dependentElements?.each { element ->
	elementCollection.add(element.@schemaLocation.text())
      }
    }
}