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
   * ant wsimport parameter flags
   * see #https://jax-ws.java.net/${jaxws-version}/docs/wsimportant.html#
   */
  @Input
  boolean verbose, keep, xnocompile, fork, xdebug

  /**
   * ant wsimport parameters
   * see #https://jax-ws.java.net/${jaxws-version}/docs/wsimportant.html#
   */
  @Input
  String target, wsdlLocation

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
   * list of specific episode file NAMES ONLY, to get from episode directory
   */
  @Input
  List episodes

  @TaskAction
  void parseWsdl() { 
    log.info("parsing wsdl...\n wsdl path is {} ...\n destination directory is {}", getWsdl(), getDestination())
    
    ant.taskdef (name : 'wsimport', classname: 'com.sun.tools.ws.ant.WsImport', classpath: project.configurations[WsdlPlugin.WSDL_CONFIGURATION_NAME].asPath)

    ant.wsimport (
    wsdl            : getWsdl().path,
    verbose         : getVerbose(),
    sourcedestdir   : getDestinationDirectory().path,
    keep            : getKeep(),
    wsdlLocation    : getWsdlLocation(),
    xnocompile      : getXnocompile(),
    fork            : getFork(),
    xdebug          : getXdebug(),
    target          : getTarget()) {
      getEpisodes().each { episode ->
	log.debug("binding with file {}.episode in path {}", episode, getEpisodeDirectory().path)
	binding(dir : getEpisodeDirectory().path, includes : "${episode}.episode")
      }
    }
  }
}