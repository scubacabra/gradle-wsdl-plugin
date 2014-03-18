package com.jacobo.gradle.plugins.tasks

import org.gradle.api.tasks.bundling.War

import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.InputFiles

import org.gradle.api.file.FileCollection

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

/**
 * Assembles a WAR archive for the wsdl plugin, adds extra functionality to war
 * Populates the WAR with the wsdl folder defined in the build directory
 * Populates the WAR with the schema folder defined in the build directory
 *
 * @author djmijares
 */
class WsdlWar extends War {

  static final Logger log = Logging.getLogger(War.class)

  /**
   * wsdl dependencies (absolute files) that need to be copied into the archive
   */
  @InputFiles
  FileCollection wsdlDependencies


  /**
   * I had to do this in the constructor, it was impossible for me, at least
   * when I attemped this, to call super, and then have another method
   * do the into and from.  That was how I did all of the other tasks, but
   * alas, not this one.
   */
  WsdlWar() {
    super()
    log.debug("Calling war constructor")
    webInf.into('') {
      from {
	getWsdlDependencies()
      }
    }
  }

  // @TaskAction
  // void start() {
  //   def wsdlDirectory = new File(project.rootDir, getWsdlFolder())
  //   def schemaDirectory = new File(project.rootDir, getSchemaFolder())
  //   def wsdlFile = converter.convert(project.name, wsdlDirectory)
  //   def resolvedDependencies = dependencyResolver.resolveDependencies(wsdlFile)
  //   def resolvedWsdlPaths = childrenPathResolver.resolvePaths(wsdlDirectory,
  // 							      resolvedDependencies)
  //   def resolvedSchemaPaths = childrenPathResolver.resolvePaths(schemaDirectory,
  // 								resolvedDependencies)
  //   copy.execute(ant, ["toDir": getWsdlDirectory(), "fromDir": wsdlDirectory,
  // 		       "includeFiles": resolvedWsdlPaths.join(',')])
  //   copy.execute(ant, ["toDir": getSchemaDirectory(), "fromDir": schemaDirectory,
  // 		       "includeFiles": resolvedSchemaPaths.join(',')])
  // }
}