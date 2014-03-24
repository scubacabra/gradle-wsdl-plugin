package com.jacobo.gradle.plugins.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

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
   * user supplied episode Files to bind with this ant action
   */
  @InputFiles
  FileCollection episodeFiles

  /**
   * The dependencies of the wsdl, used to see if this task is up to date or not
   */
  @InputFiles
  FileCollection wsdlDependencies

  /**
   * ant wsimport parameter -- where to place generated source
   * see #https://jax-ws.java.net/${jaxws-version}/docs/wsimportant.html#
   */
  @OutputDirectory
  File destinationDirectory

  /**
   * wsimport service executor
   */
  AntExecutor antExecutor

  @TaskAction
  void start() {
    def wsdlConfiguration = project.configurations[
      WsdlPlugin.WSDL_CONFIGURATION_NAME
    ]
    
    getAntExecutor().execute(ant,
			     ["wsdl": getWsdlFile(),
			      "extension": project.wsdl.wsimport,
			      "destinationDir": getDestinationDirectory(),
			      "classpath": wsdlConfiguration.asPath,
			      "episodeFiles": getEpisodeFiles().files
			     ]
			    )

  }
}