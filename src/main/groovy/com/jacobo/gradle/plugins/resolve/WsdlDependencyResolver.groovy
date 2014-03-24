package com.jacobo.gradle.plugins.resolve

import com.google.inject.Inject

import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

import com.gradle.plugins.jacobo.schema.factory.DocumentFactory

class WsdlDependencyResolver implements DependencyResolver {
  private static final Logger log = Logging.getLogger(WsdlDependencyResolver.class)

  Set<File> resolvedDependencies = [] as Set
  Set<File> unresolvedFiles = [] as Set
  DocumentFactory documentFactory

  @Inject
  WsdlDependencyResolver(DocumentFactory docFactory) {
    documentFactory = docFactory
  }

  /**
   * Recursively scan WSDL document and find the Files it depends on
   * #unresolvedFiles is used to temporarily store the files that this
   * document depends on, that haven't been parsed and resolved yet
   *
   * As long as this structure contains elements, the resolving will continue
   * 
   * @param wsdlDocument the wsdl document to resolve
   * @return Set of Files this document depends on
   */
  public Set<File> resolveDependencies(File wsdlDocument){
    this.unresolvedFiles.add(wsdlDocument)
    log.info("resolving dependencies for wsdl file '{}'", wsdlDocument)
    while(!this.unresolvedFiles.isEmpty()) {
      def documentFile = this.unresolvedFiles.iterator().next()
      log.debug("creating document for file '{}'", documentFile)
      def schemaDocument = documentFactory.createDocument(documentFile)
      // remove from unresolved, so it doesn't run through again
      this.unresolvedFiles.remove(documentFile)
      // schemaDocument is now resolved
      this.resolvedDependencies.add(schemaDocument.documentFile)
      log.debug("retrieving '{}' document dependencies from document",
		schemaDocument.documentDependencies.size())
      schemaDocument.documentDependencies.values().each { dependentFile ->
	// this dependent file has already been resolved/parsed 
	if (this.resolvedDependencies.contains(dependentFile)) { 
	  log.debug("'{}' has already been resolved, skipping repeat resolution",
		   dependentFile)
	  return true
	}
	this.unresolvedFiles.add(dependentFile)
      }
    }
    log.debug("'{}' has '{}' dependencies", wsdlDocument,
	      this.resolvedDependencies.size())
    return this.resolvedDependencies
  }
}