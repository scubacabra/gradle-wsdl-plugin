package com.jacobo.gradle.plugins.ant

import com.jacobo.gradle.plugins.extension.WsImportExtension

class AntWsImport implements AntExecutor {

  /**
   * wsdl file reference
   */
  File wsdl
  
  /**
   * used for ant.taskdef classpath
   */
  def configurationPath

  /**
   * ant wsimport parameter -- where to place generated source
   * see #https://jax-ws.java.net/${jaxws-version}/docs/wsimportant.html#
   */
  File destinationDirectory

  /**
   * ant wsimport parameter flags
   * see #https://jax-ws.java.net/${jaxws-version}/docs/wsimportant.html#
   */
  boolean verbose, keep, xnocompile, fork, xdebug

  /**
   * ant wsimport parameters
   * see #https://jax-ws.java.net/${jaxws-version}/docs/wsimportant.html#
   */
  String target, wsdlLocation

  /**
   * Directory where to find episode files
   */
  File epsiodeDirectory

  /**
   * list of specific episode file NAMES ONLY, to get from episode directory
   */
  List episodes

  /**
   * construct this ant service with the WsImportExtension
   */
  AntWsImport(File wsdlFile, configurationPath, WsImportExtension wsimportExtension) {
    wsdl = wsdlFile
    this.configurationPath = configurationPath
    destinationDirectory = wsimportExtension.sourceDestinationDirectory
    verbose = wsimportExtension.verbose
    keep = wsimportExtension.keep
    xnocompile = wsimportExtension.xnocompile
    fork = wsimportExtension.fork
    xdebug = wsimportExtension.xdebug
    target = wsimportExtension.target
    wsdlLocation = wsimportExtension.wsdlLocation
    episodeDirectory = wsimportExtension.episodeDirectory
    episodes = wsimportExtension.episodes
  }

  public void execute() {
    log.info("parsing wsdl '{}' with destination directory of '{}'",
	     wsdl, destinationDirectory)
    
    ant.taskdef (name : 'wsimport',
		 classname: 'com.sun.tools.ws.ant.WsImport',
		 classpath: configurationPath)

    ant.wsimport ( wsdl            : wsdl.path,
		   verbose         : verbose,
		   sourcedestdir   : destinationDirectory.path,
		   keep            : keep,
		   wsdlLocation    : wsdlLocation,
		   xnocompile      : xnocompile,
		   fork            : fork,
		   xdebug          : xdebug,
		   target          : target
		 ) { episodes.each { episode ->
		   log.debug("binding  '{}.episode' at location '{}'", episode, episodeDirectory.path)
		   binding(dir : episodeDirectory.path, includes : "${episode}.episode")
      }
    }
  }
}