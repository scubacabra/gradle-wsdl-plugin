package com.jacobo.gradle.plugins.copy

import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

import groovy.util.AntBuilder

class WsdlCopyDependencies implements CopyDependencies {
  private static final Logger log = Logging.getLogger(WsdlCopyDependencies.class)

  public void copy(AntBuilder ant, File toDir, File fromDir, String includeFiles) {
    log.debug("copying files '{}' to '{}' from '{}'", includeFiles, toDir, fromDir)
    ant.copy(toDir: toDir) {
      fileset(dir: fromDir, includes: includes)
    }
  }
}