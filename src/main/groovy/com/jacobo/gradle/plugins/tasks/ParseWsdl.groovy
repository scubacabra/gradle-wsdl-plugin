package com.jacobo.gradle.plugins.tasks

import com.jacobo.gradle.plugins.WsdlPlugin

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

import org.gradle.api.tasks.TaskAction
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory

import com.jacobo.gradle.plugins.ant.AntWsImport

/**
 * Parses the wsdl file with wsimport ant task, version 2.1, implementation 2.1.5
 * Can access jaxb episode binding folders and user listed episode bindings bound at task execution time
 * @author djmijares
 * Created: Mon Jan 07 18:08:42 EST 2013
 */
class ParseWsdl extends DefaultTask { 
  static final Logger log = Logging.getLogger(ParseWsdl.class)

  /**
   * Wsdl file reference (absolute)
   */
  @InputFile
  File wsdlFile

  /**
   * ant wsimport parameter -- where to place generated source
   * see #https://jax-ws.java.net/${jaxws-version}/docs/wsimportant.html#
   */
  @OutputDirectory
  File destinationDirectory

  /**
   * Directory where to find episode files
   */
  @InputDirectory
  File episodeDirectory

  /**
   * wsimport service executor
   */
  AntExecutor wsimport

  @TaskAction
  void start() {
    wsimport.execute()
  }
}