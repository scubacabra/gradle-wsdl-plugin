package com.jacobo.gradle.plugins.model

import com.jacobo.gradle.plugins.util.ListUtil

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

class XsdSlurper {
  private static final Logger log = Logging.getLogger(XsdSlurper.class)  
  
  def documentName
  File currentDir
  def xsdImports = []
  def xsdIncludes = []

  /**
   * @param xsd the xml slurped document to grab data from
   * grabs dependency data from statments in the XSD
   */
  def grabXsdDependencies(xsd) { 
    log.debug("starting to grab XSD dependencies for {}", xsd)
    grabXsdIncludedDependencies(xsd)
    grabXsdImportedDependencies(xsd)
    log.debug("grabbed all XSD dependencies for {}", xsd)
  }

  /**
   * @param xsd the xml slurped document to grab data from
   * grabs schema Locations from import statements in the XSD
   */
  def grabXsdImportedDependencies(xsd) { 
    log.debug("resolving xsd 'imported' dependencies for {}", xsd)
    processXsdDependencyLocations(xsd?.import)
    log.debug("resolved all xsd 'imported' dependencies for {}", xsd)
  }

  /**
   * @param xsd the xml slurped document to grab data from
   * grabs schema dependencies from include statements in the XSD
   */
  def grabXsdIncludedDependencies(xsd) { 
    log.debug("resolving xsd 'include' dependencies for {}", xsd)
    processXsdDependencyLocations(xsd?.include)
    log.debug("resolved all xsd 'include' dependencies for {}", xsd)
  }

  /**
   * @param xsdSlurperElement is the array of elements taken from the #XmlSlurper for the given element
   * adds the schema location of the import/include statement to the appropriate list
   * @see #importedNamespaces List
   */
  def processXsdDependencyLocations = { xsdSlurperElement ->
    xsdSlurperElement?.each { xsdElement ->
      def dependencyType = xsdElement.name()
      log.debug("the slurper element type is of {}", dependencyType)
      def dependentSchemaLocation = xsdElement.@schemaLocation.text()
      log.debug("the dependeny schema location is at {}", dependentSchemaLocation)
      if (dependencyType == "import") { //either going to be import or include
	ListUtil.addElementToList(xsdImports, dependentSchemaLocation)
      } else {
	ListUtil.addElementToList(xsdIncludes, dependentSchemaLocation)
      }
    }
  }

  /**
   * Gathers all relative locations belonging to this instance and packages up into one list
   * @return List of all locations from fields #xsdImports and #xsdIncludes
   */
  def gatherAllRelativeLocations() { 
    def returnList = []
    returnList.addAll(xsdImports)
    returnList.addAll(xsdIncludes)
    return returnList
  }
}