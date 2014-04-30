package org.gradle.jacobo.plugins.ant

import org.gradle.api.file.FileCollection
import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

import groovy.util.AntBuilder

import org.gradle.jacobo.plugins.extension.WsImportExtension

class AntWsImport implements AntExecutor {
  static final Logger log = Logging.getLogger(AntWsImport.class)

  /**
   * run the ant wsimport task an #wsdl
   * @param ant the ant builder to build and execute
   * @param arguments a map of arguments to use in the ant file
   *   "wsdl"=> the file to parse with wsimport
   *   "extension" => WsImportExtension wsdl plugins wsimport extension for configuration
   *   "destinationDir" => destinationDirectory for the wsimport output to go
   *   "classpath" => configuration path as String classpath for the ant task def as defined by the wsdl-configuration.asPath
   *   "episodeFiles" => set of episode files (absolute paths) to bind to this wsimport action
   */
  public void execute(AntBuilder ant, Object... arguments) {
    def wsdl = arguments[0]
    def extension = arguments[1]
    def destinationDir = arguments[2]
    def classpath = arguments[3]
    def episodes = arguments[4]

    log.info("parsing wsdl '{}' with destination directory of '{}'",
	     wsdl, destinationDir)
    
    ant.taskdef (name : 'wsimport',
		 classname: 'com.sun.tools.ws.ant.WsImport',
		 classpath: classpath)

    ant.wsimport ( wsdl            : wsdl.path,
		   verbose         : extension.verbose,
		   sourcedestdir   : destinationDir.path,
		   keep            : extension.keep,
		   wsdlLocation    : extension.wsdlLocation,
		   xnocompile      : extension.xnocompile,
		   fork            : extension.fork,
		   xdebug          : extension.xdebug,
		   target          : extension.target
		 )
    { 
      episodes.addToAntBuilder(ant, 'binding', FileCollection.AntType.FileSet)
    }

  }
}