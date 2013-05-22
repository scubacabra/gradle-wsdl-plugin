package com.jacobo.gradle.plugins.model

import com.jacobo.gradle.plugins.util.ListHelper
import com.jacobo.gradle.plugins.reader.DocumentReader

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
   * List of any schema locations left to parse and go through, when this is empty, the processing can return
   */
  def schemaLocationsToParse = []

  /**
   * List of all absolute Path Dependencies to be returned to the caller
   */
  def absolutePathDependencies = []

  /**
   * resolves the wsdl dependencies starting at #startingWsdl . While there are files in
   * @see #schemaLocationsToParse,
   * keep slurping documents and gather dependencies for wsdl/xsd files
   * @param startingWsdl the absolute path of the starting wsdl to go through and resolve
   * @return List of #absolutePathDependencies
   */
  def List resolveWSDLDependencies(File startingWsdl) {
    log.info("resolving wsdl dependencies starting at {}", startingWsdl)
    schemaLocationsToParse << startingWsdl
    def slurper
    def relativeSlurpedLocations
    while(schemaLocationsToParse) { 
      def document = schemaLocationsToParse.pop()
      log.debug("popping {} from schemaLocationsToParse list", document)
      slurper = DocumentReader.slurpDocument(document)
      log.debug("adding {} to absolute Path List", document)
      addAbsolutePathDependencies(document)
      log.debug("gathering Relative locations from slurper {}", slurper.documentName)
      relativeSlurpedLocations = slurper.gatherAllRelativeLocations() 
      log.debug("processing relative Locations and adding them to the schema to Parse List")
      processRelativeLocations(relativeSlurpedLocations, slurper)
    }
    log.debug("returning file list {}", absolutePathDependencies)
    return absolutePathDependencies
  }
   
  /**
   * Takes a #XsdSlurper or #WsdlSurper class and process that classes relativeLocations relative to the current directory of that file.
   * add to #schemaLocationsToParse
   * @param relativeLocations is a List of locations to process into the #schemaLocationsToParse and the #absolutePathDependencies
   */
  def processRelativeLocations(List relativeLocations, slurper) { 
    def currentDir = slurper.currentDir
    log.debug("current Dir for absolute File reference is {}", currentDir)
    relativeLocations.each { location ->
      def absoluteFile = getAbsoluteSchemaLocation(location, currentDir)
      log.debug("relative location is {}, absolute is {}", location, absoluteFile.path)
      addSchemaLocationToParse(absoluteFile)
    }
  }
  
  /**
   * Add file to #schemaLocationsToParse
   * @param file is the absolute file path to add to the @scheaLocationsToParse list
   */
  def addSchemaLocationToParse(File file) { 
    if (!ListHelper.isAlreadyInList(schemaLocationsToParse, file) && !ListHelper.isAlreadyInList( absolutePathDependencies, file)) {
      log.debug("added {} to schema Location to Parse List", file)
      schemaLocationsToParse << file
    }
  }

  /**
   * Add file to #absolutePathDependencies
   * @param file is the absolute file path to add to the @abosluteFileDependencies
   */
  def addAbsolutePathDependencies(File file) { 
    if (!ListHelper.isAlreadyInList(absolutePathDependencies, file)) {
      log.debug("added {} to absolute file dependencies  List", file)
      absolutePathDependencies << file
    }
  }

  /**
   * Figure out the ABSOLUTE schema location of the String relative to the parent/current directory
   * @param schemaLocation is the relative path of the schema location being called in eith the xsd:import or xsd:includes call
   * @param parentDir is the parent directory of the schema file that is currently being Xml Slurped
   * @return File absolute File path to schema Location
   */
  File getAbsoluteSchemaLocation(String schemaLocation, File parentDir) { 
    def relPath = new File(parentDir, schemaLocation)
    return new File(relPath.canonicalPath)
  }

}

