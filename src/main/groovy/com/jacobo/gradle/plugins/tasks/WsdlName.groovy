package com.jacobo.gradle.plugins.tasks

import com.jacobo.gradle.plugins.extension.WsdlPluginExtension

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException

import com.jacobo.gradle.plugins.util.WsdlNameHelper

/**
 * Process the name of this project and generates the corresponding wsdl file name and directory
 * @author djmijares
 * Created: Mon Jan 07 18:08:42 EST 2013
 */
class WsdlName extends DefaultTask { 
  static final Logger log = Logging.getLogger(WsdlName.class)

  final WsdlPluginExtension extension = project.wsdl
  
  /**
   * Name of the project
   */
  @Input
  String projectName

  /**
   * Directory where to find WSDL files (relative to projects root directory)
   */
  @Input
  File wsdlDirectory

  @TaskAction
  void findWsdlName() { 
    def wsdlName = WsdlNameHelper.generateWsdlName(getProjectName())

    log.debug("Checking that the file '{}.wsdl' exists at '{}'", wsdlName, getWsdlDirectory())

    if (!new File(getWsdlDirectory(), "${wsdlName}.wsdl").exists()) {
      throw new GradleException("File ${wsdlName}.wsdl does not exist at default location of '${getWsdlDirectory()}'")
    }

    extension.wsdlFileName = wsdlName
    extension.setWsdlPath()

    log.info("Found WSDL Name: '{}' for this project", extension.wsdlPath)
  }
} 