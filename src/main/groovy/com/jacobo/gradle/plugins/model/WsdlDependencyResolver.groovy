package com.jacobo.gradle.plugins.model

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

/**
 * This class resolves all WSDL dependencies, gathering a list of all files the main wsdl entry point includes/imports. including other WSDLS and XSDs
 *
 * Basically, starts at the top, gets all the imports/includes, then recursively goes through that unprocessed imports/includes until there are no more and you end up with a List of all the absolute Files that this particular WSDL depends on
 *
 * @author Daniel Mijares
 * @version 1.0
 */
class WsdlDepedencyResolver { 

  private static final Logger log = Logging.getLogger(WsdlDependencyResolver.class)  

  /**
   * Start WSDL File location (absolute Path) 
   */
  File wsdlFile

  /**
   * This is the parent Directory of whatever schema is being slurped
   * I personally don't think this should be part of this class, but eh.
   */
  File parentDirectory

  /**
   * List of any schema locations left to parse and go through, when this is empty, the processing can return
   */
  def schemaLocationsToParse = []

  /**
   * Utility function that checks if a @List has the value @input
   */
  static boolean isAlreadyInList(List list, File input) { 
    return list.contains(input)
  }

  /**
   * @param file is the absolute file path to add to the @scheaLocationsToParse list
   */
  def addSchemaLocationToParse(File file) { 
      log.debug("added {} to schema Location to Parse List", file)
      schemaLocationsToParse << file
  }

  /**
   * @param it is the import Object taken from the #XmlSlurper
   * addes the schema location of the import/include statement to the schemaLocationToParse list, if it is unique to the list
   * @see #importedNamespaces List
   */
  def locationClosure = { it ->
    def location = it.@schemaLocation.text()
    def absoluteFile = getAbsoluteSchemaLocation(location, parentDirectory) 
    if (isAlreadyInList( schemaLocationsToParse, absoluteFile)) { 
      log.debug(" schema location is {}, and the parentDirectoryectory (Parent Directory) is {}", absoluteFile, parentDirectory)
      addSchemaLocationToParse(absoluteFile)
    } 
  }

  /**
   * @param xmlDoc the xml slurped document to gether data from
   * gathers schema Locations from import/include statments in the schema
   */
  def getDependencies (xmlDoc) { 
    log.debug("resolving imports")
    xmlDoc?.import?.each locationClosure
    log.debug("resolving includes")
    xmlDoc?.include?.each locationClosure
  }

  /**
   * resolves the wsdl dependencies starting at
   * @see #wsdlFile
   * While there are files in
   * @see #schemaLocationsToParse
   * keep slurping documents and gather schema locations for imports/includes
   */
  def resolveWSDLDependencies() {
    parentDirectory = wsdlFile.parentFile
    log.info("resolving wsdl dependencies starting at {}", wsdlFile)
    def xmlDoc = new XmlSlurper().parse(wsdlFile)
    getDependencies(xmlDoc)
    while(schemaLocationsToParse) { 
      def document = schemaLocationsToParse.pop()
      log.debug("popping {} from schemaLocationsToParse list", document)
      xmlDoc = new XmlSlurper().parse(document)
      parentDirectory = document.parentFile
      getDependencies(xmlDoc)
    }
  }

  /**
   * @param schemaLocation is the relative path of the schema location being called in eith the xsd:import or xsd:includes call
   * @param parentDir is the parent directory of the schema file that is currently being Xml Slurped
   * @return File absolute File path to schema Location
   */
  File getAbsoluteSchemaLocation(String schemaLocation, File parentDir) { 
    def relPath = new File(parentDir, schemaLocation)
    return new File(relPath.canonicalPath)
  }

}

