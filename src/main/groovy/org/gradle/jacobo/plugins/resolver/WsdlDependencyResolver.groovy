package org.gradle.jacobo.plugins.resolver

import com.google.inject.Inject

import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

import org.gradle.jacobo.schema.factory.DocumentFactory

/**
 * Resolves a WSDL files dependencies.
 * @see org.gradle.jacobo.plugins.extension.WsdlPluginExtension#wsdlFile
 */
class WsdlDependencyResolver implements DependencyResolver {
  private static final Logger log = Logging.getLogger(WsdlDependencyResolver.class)
  
  /**
   * Resolved WSDL dependencies
   */
  Set<File> resolvedDependencies = [] as Set

  /**
   * Unresolved WSDL dependencies
   */
  Set<File> unresolvedFiles = [] as Set
  
  /**
   * Generates {@code BaseSchemaDocument}'s.
   * @see org.gradle.jacobo.schema.BaseSchemaDocument
   */
  DocumentFactory documentFactory

  /**
   * Construct this resolver with a {@code DocumentFactory}.
   */
  @Inject
  WsdlDependencyResolver(DocumentFactory docFactory) {
    documentFactory = docFactory
  }

  /**
   * Resolves dependencies of {@code File}.
   * Slurp WSDL file's dependencies and recursively slurp the dependencies
   * of those dependencies until all dependencies have been found.
   * <p>
   * Newest slurped dependencies are classified as <i>unresolved</i> until they
   * are slurped themselves.
   *
   * @param wsdlDocument  WSDL file to resolve
   * @return Set of files this document depends on
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