package com.jacobo.gradle.plugins.model

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

class WsdlWarRelativePathResolver { 

  private static final Logger log = Logging.getLogger(WsdlWarRelativePathResolver.class)
  
  String into, from
  List<File> resolvedFiles = []

  static List<WsdlWarRelativePathResolver> resolveRelativePathsToWar(File rootDir, List<File> includeFiles) {
    def resolvedPaths = []
    includeFiles.each { resolveFile ->
      def relPath = resolveFile.parentFile.path - rootDir.path - "/"
      def resolved = resolvedPaths.find { it.into == relPath }
      if(resolved) {
	log.debug("found resolved object {} , into {}, from {}, resolved Files {} for this relative Path {} and resolved File {} already", resolved, resolved.from, resolved.into, resolved.resolvedFiles, relPath, resolveFile)
	resolved.resolvedFiles << resolveFile
      } else {
	log.debug("no resolved object {} for this relative Path {} and resolved file {} yet, adding a new one", resolved, relPath, resolveFile)
	resolvedPaths << new WsdlWarRelativePathResolver(into: relPath, resolvedFiles: [resolveFile], from: resolveFile.parentFile.path )
      }
    }
    return resolvedPaths
  }
  
  public String toString() {
    def out = "from ${from}, into ${into} \n"
    resolvedFiles.each {
      out += "${it}:"
    }
    return out
  }
}