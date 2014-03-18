package com.jacobo.gradle.plugins.tasks

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

import org.gradle.api.tasks.TaskAction
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory

import com.jacobo.gradle.plugins.WsdlPlugin
import com.jacobo.gradle.plugins.ant.AntExecutor

/**
 * Parses the wsdl file with wsimport ant task, version 2.1, implementation 2.1.5
 * Can access jaxb episode binding folders and user listed episode bindings bound at task execution time
 * @author djmijares
 * Created: Mon Jan 07 18:08:42 EST 2013
 */
class WsdlWsImport extends DefaultTask { 
  static final Logger log = Logging.getLogger(WsdlWsImport.class)

  /**
   * Wsdl file reference (absolute)
   */
  @InputFile
  File wsdlFile

  /**
   * user episode Files to bind to
   */
  @InputFiles
  def episodeFiles

  @InputFiles
  def wsdlDependencies

  /**
   * ant wsimport parameter -- where to place generated source
   * see #https://jax-ws.java.net/${jaxws-version}/docs/wsimportant.html#
   */
  @OutputDirectory
  File destinationDirectory

  /**
   * wsimport service executor
   */
  AntExecutor wsimport

  @TaskAction
  void start() {
    def wsdlConfiguration = project.configurations[
      WsdlPlugin.WSDL_CONFIGURATION_NAME
    ]
    
    log.info("doing it on '{}'", getWsdlFile())
    wsimport.execute(ant,
    		     ["wsdl":wsdlFile, "extension":project.wsdl.wsimport,
    		      "classpath": wsdlConfiguration.asPath])
  }
}