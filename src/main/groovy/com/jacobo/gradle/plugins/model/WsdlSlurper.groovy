package com.jacobo.gradle.plugins.model

import com.jacobo.gradle.plugins.util.ListUtil

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

class WsdlSlurper extends XsdSlurper {
  private static final Logger log = Logging.getLogger(WsdlSlurper.class)  

  def wsdlImports = []
  
  /**
   * @param wsdl the XmlSlurper class to gather data from
   * gathers schema Locations from import/include statments in the wsdl Doc
   * gathers data from xsd import statments, and from wsdl import statements as well. 
   */
  def grabWsdlDependencies(wsdl) { 
    log.debug("starting to grab WSDL dependencies for {}", wsdl)
    grabWsdlImportedDependencies(wsdl)    
    grabWsdlXsdDependencies(wsdl)
    log.debug("grabbed all WSDL dependencies for {}", wsdl)
  }

  /**
   * @param wsdl the XmlSlurper class to gather data from
   * gathers schema Locations from import/include statments in the wsdl Doc
   * gathers data from xsd import statments, and from wsdl import statements as well. 
   */
  def grabWsdlImportedDependencies(wsdl) { 
    log.debug("resolving this wsdl's 'imported' wsdl dependencies")
    processWsdlDependencyLocations(wsdl?.import)
    log.debug("resolved all wsdl 'imported wsdl dependencies")
  }

  /**
   * @param wsdl the XmlSlurper class to gather data from
   * gathers schema Locations from import/include statments in the wsdl Doc
   * gathers data from xsd import statments, and from wsdl import statements as well. 
   */
  def grabWsdlXsdDependencies(wsdl) { 
    log.debug("resolving this wsdls XSD Dependencies")
    log.debug("resolving 'includes' XSD dependencies")
    processXsdDependencyLocations(wsdl?.types?.schema?.include)
    log.debug("resolving 'import'(ed) XSD dependencies")
    processXsdDependencyLocations(wsdl?.types?.schema?.import)
  }

  /**
   * @param wsdlSlurperElement is the import Object taken from the #XmlSlurper
   * adds the schema location of the import/include statement to the schemaLocationToParse list, if it is unique to the list
   * @see #importedNamespaces List
   */
  def processWsdlDependencyLocations = { wsdlSlurperElement ->
    wsdlSlurperElement?.each { wsdlElement ->
      log.debug("the XML slurper element is {}", wsdlElement.name())
      def wsdlImportLocation = wsdlElement.@location.text()
      log.debug("the location is {}", wsdlImportLocation)
      ListUtil.addElementToList(wsdlImports, wsdlImportLocation)
    }
  }

  /**
   * Gathers all relative locations belonging to this instance and packages up into one list
   * @return List of all locations from fields #xsdImports and #xsdIncludes
   */
  def gatherAllRelativeLocations() { 
    def returnList = super.gatherAllRelativeLocations()
    returnList.addAll(wsdlImports)
    return returnList
  }
}