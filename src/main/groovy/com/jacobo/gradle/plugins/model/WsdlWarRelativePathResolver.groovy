package com.jacobo.gradle.plugins.model

class WsdlWarRelativePathResolver { 

  String from = ""
  String into = ""
  String include = ""

  static WsdlWarRelativePathResolver resolveRelativePathsToWar(File rootDir, File includeFile) { 
    def relPath = includeFile.parentFile.path - rootDir.path - "/" 
    return new WsdlWarRelativePathResolver(from : relPath, into: relPath, include: includeFile.name)
  }

}