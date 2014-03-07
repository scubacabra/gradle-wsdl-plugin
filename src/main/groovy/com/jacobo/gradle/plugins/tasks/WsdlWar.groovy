package com.jacobo.gradle.plugins.tasks

import org.gradle.api.tasks.bundling.War

import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

import com.jacobo.gradle.plugins.WsdlPlugin
import com.jacobo.gradle.plugins.ant.AntExecutor
import com.jacobo.gradle.plugins.convert.NameToFileConverter
import com.jacobo.gradle.plugins.resolve.DependencyResolver
import com.jacobo.gradle.plugins.resolve.ChildrenPathResolver

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
   * The name of the folder in the war that all wsdl Files will go into
   */
  @Input
  String wsdlFolder

  /**
   * The name of the folder in the war that all schema (xsd) files will go into
   */
  @Input
  String schemaFolder

  /**
   * The actual directory in the build/$webServicesOutputDir/$wsdlDirectory
   * where all the wsdl files are that this project depends on
   */
  @InputDirectory
  File wsdlDirectory

  /**
   * The actual directory in the build/$webServicesOutputDir/xsdDirectory
   * where all the xsd files are that this project depends on
   */
  @InputDirectory
  File schemaDirectory

  /**
   * project name to wsdl file converter service
   */
  NameToFileConverter converter

  /**
   * Resolves all wsdl Dependencies through XmlSlurping
   */
  DependencyResolver dependencyResolver

  /**
   * Resolver all dependencies to their children paths
   */
  ChildrenPathResolver childrenPathResolver
  
  /**
   * Ant Copy task executor
   */
  AntExecutor copy

  /**
   * I had to do this in the constructor, it was impossible for me, at least
   * when I attemped this, to call super, and then have another method
   * do the into and from.  That was how I did all of the other tasks, but
   * alas, not this one.
   */
  WsdlWar() {
    super()
    log.debug("Calling war constructor")

    webInf.into({ getWsdlFolder() }) {
      from {
	getWsdlDirectory()
      }
    }

    webInf.into({ getSchemaFolder() }) {
      from {
	getSchemaDirectory()
      }
    }
  }

  @TaskAction
  void start() {
    def wsdlDirectory = new File(project.rootDir, getWsdlFolder())
    def schemaDirectory = new File(project.rootDir, getSchemaFolder())
    def wsdlFile = converter.convert(project.name, wsdlDirectory)
    def resolvedDependencies = dependencyResolver.resolveDependencies(wsdlFile)
    def resolvedWsdlPaths = childrenPathResolver.resolvePaths(wsdlDirectory,
							      resolvedDependencies)
    def resolvedSchemaPaths = childrenPathResolver.resolvePaths(schemaDirectory,
								resolvedDependencies)
    copy.execute(ant, ["toDir": getWsdlDirectory(), "fromDir": wsdlDirectory,
		       "includeFiles": resolvedWsdlPaths])
    copy.execute(ant, ["toDir": getSchemaDirectory(), "fromDir": schemaDirectory,
		       "includeFiles": resolvedSchemaPaths])
  }
}