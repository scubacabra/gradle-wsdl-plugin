package org.gradle.jacobo.plugins.task

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

import org.gradle.jacobo.plugins.WsdlPlugin
import org.gradle.jacobo.plugins.ant.AntExecutor

/**
 * Parses the wsdl file with {@code wsimport} ant task.
 * Can bind with user provided episode bindings, reducing duplicated code.
 */
class WsdlWsImport extends DefaultTask { 
  static final Logger log = Logging.getLogger(WsdlWsImport.class)

  /**
   * Absolute path WSDL file.
   */
  @InputFile
  File wsdlFile

  /**
   * User supplied episode Files to bind with this ant task.
   */
  @InputFiles
  FileCollection episodeFiles

  /**
   * Absolute path WSDL Dependencies.
   */
  @InputFiles
  FileCollection wsdlDependencies

  /**
   * Destination directory for the generated wsimport java output.
   */
  @OutputDirectory
  File destinationDirectory

  /**
   * Executes the {@code wsimport} ant task.
   */
  AntExecutor antExecutor

  /**
   * Executes this task.
   */
  @TaskAction
  void start() {
    def wsdlConfiguration = project.configurations[
      WsdlPlugin.WSDL_CONFIGURATION_NAME
    ]
    
    getAntExecutor().execute(ant, getWsdlFile(), project.wsdl.wsimport,
			     getDestinationDirectory(),
			     wsdlConfiguration.asPath, getEpisodeFiles())
  }
}