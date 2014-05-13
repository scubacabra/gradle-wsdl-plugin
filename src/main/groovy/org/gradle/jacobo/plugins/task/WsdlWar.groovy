package org.gradle.jacobo.plugins.task

import org.gradle.api.file.FileCollection
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.bundling.War

/**
 * Assembles a WAR archive for the wsdl plugin.
 * Adds extra functionality to war by populating the WAR with the WSDL
 * dependencies as seen on the projects file system.
 * <p>
 * The root of the war will contain folders
 * <ul>
 * <li> value of {@link org.gradle.jacobo.plugins.extension.WsdlPluginExtension#wsdlFolder}
 * <li> value of {@link org.gradle.jacobo.plugins.extension.WsdlPluginExtension#schemaFolder}
 * </ul>
 * These folders will be populated where a matching entry in the WSDL's dependencies are located.
 * For small wsdl projects, this might not sound so cool, but for really huge projects, it is awesome.
 */
class WsdlWar extends War {

  static final Logger log = Logging.getLogger(War.class)

  /**
   * Absolute path WSDL dependencies that are copied into the WAR archive
   */
  @InputFiles @Optional
  FileCollection wsdlDependencies

  /**
   * Root folder location for the Wsdl files in the file system.
   * @see org.gradle.jacobo.plugins.extension.WsdlPluginExtension#wsdlFolder
   */
  @Input
  File wsdlFolder

  /**
   * Root folder location for the schema(xsd) files in the file system.
   * @see org.gradle.jacobo.plugins.extension.WsdlPluginExtension#schemaFolder
   */
  @Input
  File schemaFolder

  /**
   * Constructor to set up lazy loaded closures used to configure the WAR.
   * Setting these up in an annoted {@code @TaskAction} method, didn't yield
   * the correct results.
   */
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