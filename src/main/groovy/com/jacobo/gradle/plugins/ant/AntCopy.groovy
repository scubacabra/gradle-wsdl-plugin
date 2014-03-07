package com.jacobo.gradle.plugins.ant

import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

import groovy.util.AntBuilder

class AntCopy implements AntExecutor {
  private static final Logger log = Logging.getLogger(AntCopy.class)

  /**
   * Copies dependencies via an #AntBuilder instances copy task
   * @param ant AntBuilder to use on the copy task
   * @param arguments to configure antbuilder with keys
   *   "toDir" copy to this directory
   *   "fromDir" copy from this directory
   *   "includeFiles" file to include (comma, or space separated list of file patterns to include
   *   i.e "a.txt,b.txt,c.txt" OR "a.txt b.txt c.txt"
   */
  public void execute(AntBuilder ant, Map<String, Object> arguments) {
    File toDirectory = arguments["toDir"]
    File fromDirectory = arguments["fromDir"]
    String includeFiles = arguments["includeFiles"]
    log.debug("copying files '{}' to '{}' from '{}'", includeFiles,
	      toDirectory, fromDirectory)
    ant.copy(toDir: toDirectory) {
      fileset(dir: fromDirectory, includes: includeFiles)
    }
  }
}

