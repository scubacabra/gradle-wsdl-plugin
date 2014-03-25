package com.jacobo.gradle.plugins.tasks

import org.gradle.api.file.FileCollection
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.bundling.War

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
   * Could contain nothing, in which case, return an empty collection
   */
  @InputFiles @Optional
  FileCollection wsdlDependencies

  /**
   * Top Folder location (absolute) that contains wsdl files for this project
   */
  @Input
  File wsdlFolder

  /**
   * Top Folder location (absolute) that contains schema files for this project
   */
  @Input
  File schemaFolder

  WsdlWar() {
    super()
    // in a closure for lazy loading only executing at runtime
    // mirror file system into zipped artifact.  all *.wsdl files go under this folder name
    webInf.into( {getWsdlFolder().name} ) {
      from {
	def dependencies = getWsdlDependencies()
	def wsdlFiles = dependencies ? dependencies.filter { File file ->
	  file.name.endsWith('.wsdl')
	} : []
	// in case there are no wsdl files in dependencies, return empty collection
	if (wsdlFiles.isEmpty()) return project.files()
	// workaround because atree.matching{include{it.file in files.files}}
	// doesn't loop over all elements
	// get relative paths to the folder to pass as include params
	def relPaths = wsdlFiles.files.collect { File file ->
	  file.path - (getWsdlFolder().path + File.separator)
	}
	project.fileTree(dir: getWsdlFolder(), include: relPaths)
      }
    }
    // in a closure for lazy loading only executing at runtime
    // mirror file system into zipped artifact.  all *.xsd files go under this folder name
    webInf.into( {getSchemaFolder().name } ) {
      from {
	def dependencies = getWsdlDependencies()
	def schemaFiles = dependencies ? dependencies.filter { File file ->
	  file.name.endsWith('.xsd')
	} : []
	// in case there are no xsd files in dependencies, return empty collection
	if (schemaFiles.isEmpty()) return project.files()
	// workaround because atree.matching{include{it.file in files.files}}
	// doesn't loop over all elements
	// get relative paths to the folder to pass as include params
	def relPaths = schemaFiles.files.collect { File file ->
	  file.path - (getSchemaFolder().path + File.separator)
	}
	project.fileTree(dir: getSchemaFolder(), include: relPaths)
      }
    }
  }
}