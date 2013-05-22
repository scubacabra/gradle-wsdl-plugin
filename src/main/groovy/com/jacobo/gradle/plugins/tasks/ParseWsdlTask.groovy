package com.jacobo.gradle.plugins.tasks

import com.jacobo.gradle.plugins.WsdlExtension
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
class ParseWsdlTask extends DefaultTask { 
  static final Logger log = Logging.getLogger(ParseWsdlTask.class)

  @InputFile
  File wsdl

  @Input
  boolean verbose, keep, xnocompile, fork, xdebug

  @Input
  String target, wsdlLocation

  @InputDirectory
  File episode

  @Input
  List episodes

  @OutputDirectory
  File destination

  @TaskAction
  void parseWsdl() { 
    log.info("parsing wsdl...\n wsdl path is {} ...\n destination directory is {}", getWsdl(), getDestination())
    
    ant.taskdef (name : 'wsimport', classname: 'com.sun.tools.ws.ant.WsImport', classpath: project.configurations[WsdlPlugin.WSDL_CONFIGURATION_NAME].asPath)

    ant.wsimport (
    wsdl            : getWsdl().path,
    verbose         : getVerbose(),
    sourcedestdir   : getDestination().path,
    keep            : getKeep(),
    wsdlLocation    : getWsdlLocation(),
    xnocompile      : getXnocompile(),
    fork            : getFork(),
    xdebug          : getXdebug(),
    target          : getTarget()) {
      getEpisodes().each { episode ->
	log.debug("binding with file {}.episode in path {}", episode, getEpisode().path)
	binding(dir : getEpisode().path, includes : "${episode}.episode")
      }
    }
  }
}