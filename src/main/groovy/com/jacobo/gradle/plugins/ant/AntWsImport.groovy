package com.jacobo.gradle.plugins.ant

import groovy.util.AntBuilder

import com.jacobo.gradle.plugins.extension.WsImportExtension

class AntWsImport implements AntExecutor {

  /**
   * run the ant wsimport task an #wsdl
   * @param ant the ant builder to build and execute
   * @param arguments a map of arguments to use in the ant file
   *   "wsdl"=> the file to parse with wsimport
   *   "extension" => WsImportExtension wsdl plugins wsimport extension for configuration
   *   "classpath" => configuration path as String classpath for the ant task def as defined by the wsdl-configuration.asPath
   */
  public void execute(AntBuilder ant, Map<String, Object> arguments) {
    def wsdl = arguments["wsdl"]
    def extension = arguments["extension"]
    def classpath = arguments["classpath"]

    log.info("parsing wsdl '{}' with destination directory of '{}'",
	     wsdl, extension.destinationDirectory)
    
    ant.taskdef (name : 'wsimport',
		 classname: 'com.sun.tools.ws.ant.WsImport',
		 classpath: classpath)

    ant.wsimport ( wsdl            : wsdl.path,
		   verbose         : extension.verbose,
		   sourcedestdir   : extenstion.destinationDirectory.path,
		   keep            : extension.keep,
		   wsdlLocation    : extension.wsdlLocation,
		   xnocompile      : extension.xnocompile,
		   fork            : extension.fork,
		   xdebug          : extension.xdebug,
		   target          : extension.target
		 ) { extension.episodes.each { episode ->
		   log.debug("binding  '{}.episode' at location '{}'", episode,
			     extension.episodeDirectory.path)
		   binding(dir : extension.episodeDirectory.path,
			   includes : "${episode}.episode") }
    }
  }
}