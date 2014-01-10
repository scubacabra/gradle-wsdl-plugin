package com.jacobo.gradle.plugins.model

import com.jacobo.gradle.plugins.reader.DocumentReader
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

/**
 * This class resolves all WSDL dependencies, gathering a list of all files (Absolute Paths) that the main wsdl entry point includes/imports. including other WSDLS and XSDs
 *
 * Basically, starts at the top, gets all the imports/includes, then recursively goes through that unprocessed imports/includes until there are no more and you end up with a List of all the absolute Files that this particular WSDL depends on
 *
 * @author jacobono
 * @version 1.0
 */
class WsdlDependencyResolver { 

  private static final Logger log = Logging.getLogger(WsdlDependencyResolver.class)  

  /**
   * List of File objects representing the unresolved Dependencies left to resolve before an exit is possible from #resolveProjectDependencies
   */
  def unresolvedDependencies = [] as Set

  /**
   * Set with File object, containing aboslute Paths as entries
   */
  def projectDependencies = [] as Set

  /**
   * resolves the wsdl dependencies starting at #projectWsdl . While there are files in
   * @see #unresolvedDependencies,
   * keep slurping documents and gather dependencies for wsdl/xsd files
   * @param projectWsdl the absolute path of the starting wsdl to go through and resolve
   * @return List of #absolutePathDependencies
   */
  def resolveProjectDependencies(File projectWsdl) {
    log.info("Resolving project dependencies -- starting point is '{}'", projectWsdl)
    unresolvedDependencies.add(projectWsdl)
    while(!this.unresolvedDependencies.isEmpty()) { 
      def document = this.unresolvedDependencies.iterator().next()
      def slurpedDocument = DocumentReader.slurpDocument(document)
      this.projectDependencies.add(document)
      this.resolveDocumentDependencies(slurpedDocument)
      this.unresolvedDependencies.remove(document)
    }
    log.debug("Resolved project Dependencies! -- '{}'", this.projectDependencies)
    return this.projectDependencies
  }
   
  /**
   * Takes a #XsdSlurper or #WsdlSurper class and process that classes relativeLocations relative to the current directory of that file.
   * add to #unresolvedDependencies
   * @param slurpedDocument is the slurped class to process relative locations from
   */
  def resolveDocumentDependencies(DocumentSlurper slurpedDocument) { 
    log.debug("Resolving document dependencies from document '{}' -- the current directory is '{}'", slurpedDocument.documentFile.name, slurpedDocument.documentFile.parentFile)
    slurpedDocument.documentDependencies.each { location ->
      resolveDependency(location)
    }
  }
  
  /**
   * Resolve This particular Dependency
   * If it is already resolved, no sense in performing that task again -- skip
   * @param file is the absolute file path check for resolution
   */
  def resolveDependency(File file) { 
    if (projectDependencies.contains(file)) return // already resolved/parsed
    unresolvedDependencies.add(file)
  }
}

