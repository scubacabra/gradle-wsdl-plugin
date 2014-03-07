package com.jacobo.gradle.plugins.tasks

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

import org.gradle.api.tasks.TaskAction
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory

import com.jacobo.gradle.plugins.WsdlPlugin
import com.jacobo.gradle.plugins.ant.AntExecutor
import com.jacobo.gradle.plugins.convert.NameToFileConverter

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
   * project name to wsdl file converter service
   */
  NameToFileConverter converter

  /**
   * wsimport service executor
   */
  AntExecutor wsimport

  @TaskAction
  void start() {
    def wsdlConfiguration = project.configurations[
      WsdlPlugin.WSDL_CONFIGURATION_NAME
    ]

    wsdlFile = converter.convert(project.name,
				 new File(project.rootDir,
					  project.wsdl.wsdlFolder))
    wsimport.execute(ant,
		     ["wsdl":wsdlFile, "extension":project.wsdl.wsimport,
		      "classpath": wsdlConfiguration.asPath])
  }
}