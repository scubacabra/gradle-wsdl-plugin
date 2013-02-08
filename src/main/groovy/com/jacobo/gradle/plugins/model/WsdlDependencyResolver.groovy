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
class WsdlDependencyResolver { 

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
   * List of all absolute Path Dependencies to be returned to the caller
   */
  def absolutePathDependencies = []
  
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
    if (!isAlreadyInList( schemaLocationsToParse, file) && !isAlreadyInList( absolutePathDependencies, file)) { 
      log.debug(" schema location is {}, and the parentDirectoryectory (Parent Directory) is {}", file, parentDirectory)
      log.debug("added {} to schema Location to Parse List", file)
      schemaLocationsToParse << file
    }
  }

  /**
   * @param file is the absolute file path to add to the @abosluteFileDependencies
   */
  def addAbsolutePathDependencies(File file) { 
    if (!isAlreadyInList( absolutePathDependencies, file)) { 
      log.debug("added {} to absolute file dependencies  List", file)
      absolutePathDependencies << file
    }
  }

  /**
   * @param it is the import Object taken from the #XmlSlurper
   * addes the schema location of the import/include statement to the schemaLocationToParse list, if it is unique to the list
   * @see #importedNamespaces List
   */
  def xsdLocationClosure = { it ->
    log.info("the xml slurper element is {}", it)
    def location = it.@schemaLocation.text()
    log.info("the location is {}", location)
    def absoluteFile = getAbsoluteSchemaLocation(location, parentDirectory) 
    addSchemaLocationToParse(absoluteFile)
  }

  /**
   * @param it is the import Object taken from the #XmlSlurper
   * addes the schema location of the import/include statement to the schemaLocationToParse list, if it is unique to the list
   * @see #importedNamespaces List
   */
  def wsdlLocationClosure = { it ->
    log.info("the xml slurper element is {}", it)
    def location = it.@location.text()
    log.info("the location is {}", location)
    def absoluteFile = getAbsoluteSchemaLocation(location, parentDirectory) 
    addSchemaLocationToParse(absoluteFile)
  }

  /**
   * @param xmlDoc the xml slurped document to gether data from
   * gathers schema Locations from import/include statments in the XSD schema
   */
  def getXsdDependencies (xmlDoc) { 
    log.debug("resolving xsd imports")
    xmlDoc?.import?.each xsdLocationClosure
    log.debug("resolving xsd includes")
    xmlDoc?.include?.each xsdLocationClosure
  }
  
  /**
   * @param wsdlDoc the xml slurped document to gether data from
   * gathers schema Locations from import/include statments in the wsdl Doc
   * gathers data from xsd import statments, and from wsdl import statements as well. 
   */
  def getWsdlDependencies(wsdlDoc) { 
    log.debug("resolving wsdl xsd imports")
    wsdlDoc?.types?.schema?.import?.each xsdLocationClosure
    log.debug("resolving wsdl imports")
    wsdlDoc?.import?.each wsdlLocationClosure
  }

  /**
   * resolves the wsdl dependencies starting at
   * @see #wsdlFile
   * While there are files in
   * @see #schemaLocationsToParse
   * keep slurping documents and gather schema locations for imports/includes
   * @return List of #absolutePathDependencies
   */
  def List resolveWSDLDependencies() {
    log.info("resolving wsdl dependencies starting at {}", wsdlFile)
    parseDocument(wsdlFile)
    while(schemaLocationsToParse) { 
      def document = schemaLocationsToParse.pop()
      log.debug("popping {} from schemaLocationsToParse list", document)
      parseDocument(document)
    }
    log.debug("returning file list {}", absolutePathDependencies)
    return absolutePathDependencies
  }

  /**
   * common compute chain for resolving dependencies
   * @param document is a File to be slurped
   * set parent, slurp, gather deps, and add to absolute deps
   */
  def parseDocument(File document) { 
    log.info("file is {}", document)
    parentDirectory = document.parentFile
    log.info("parent File is {}", parentDirectory)
    def xmlDoc = new XmlSlurper().parse(document)
    if(document.name.split("\\.")[-1] == 'xsd') {
      getXsdDependencies(xmlDoc)	
    } else {
      getWsdlDependencies(xmlDoc)
    }
    addAbsolutePathDependencies(document)
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

