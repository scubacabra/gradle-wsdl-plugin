package org.gradle.jacobo.plugins.ant

import org.gradle.api.file.FileCollection
import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

import groovy.util.AntBuilder

import org.gradle.jacobo.plugins.extension.WsImportExtension

/**
 * Defines and executes the {@code wsimport} ant task.
 */
class AntWsImport implements AntExecutor {
  static final Logger log = Logging.getLogger(AntWsImport.class)

  /**
   * Defines and executes the ant {@code wsimport} task.
   * A variable list of arguments is passed in containing data
   * to configure this task. In order, they are:
   * <ul>
   * <li> {@code wsdl} => WSDL file to parse in {@code wsimport} task
   * <li> {@code extension} => This plugins extension {@code WsImportExtension}
   * <li> {@code destinationDir} => where to send the wsimport generated code
   * <li> {@code classpath} => String of classpath to set up via this plugins
   *      configuration dependencies
   * <li> {@code episodes} => {@code FileCollection} holding the episode files
   *      to bind with Uses {@code addToAntBuilder} to make this binding super easy.
   * </ul>
   * @param ant  {@code AntBuilder} to configure and execute
   * @param arguments  variable arguments to configure the {@code wsimport} task
   * @see org.gradle.jacobo.plugins.extension.WsImportExtension
   * @see org.gradle.api.file.FileCollection
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