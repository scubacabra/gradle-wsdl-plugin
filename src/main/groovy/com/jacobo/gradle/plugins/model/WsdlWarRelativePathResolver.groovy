package com.jacobo.gradle.plugins.model

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

class WsdlWarRelativePathResolver { 

  private static final Logger log = Logging.getLogger(WsdlWarRelativePathResolver.class)
  
  String from = ""
  String into = ""
  String include = ""

  static WsdlWarRelativePathResolver resolveRelativePathsToWar(File rootDir, File includeFile) { 
    def relPath = includeFile.parentFile.path - rootDir.path - "/" 
    def resolver = new WsdlWarRelativePathResolver(from : includeFile.parentFile.path, into: relPath, include: includeFile.name)
    log.info("for file {}, from : {}, into: {}, include : {}", includeFile, resolver.from, resolver.into, resolver.include)
    return resolver
  }

}