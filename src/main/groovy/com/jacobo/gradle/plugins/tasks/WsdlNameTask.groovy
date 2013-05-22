package com.jacobo.gradle.plugins.tasks

import com.jacobo.gradle.plugins.WsdlPluginExtension

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException

import com.jacobo.gradle.plugins.model.WsdlName

/**
 * Process the name of this project and generates the corresponding wsdl file name and directory
 * @author djmijares
 * Created: Mon Jan 07 18:08:42 EST 2013
 */
class WsdlNameTask extends DefaultTask { 
  static final Logger log = Logging.getLogger(WsdlNameTask.class)
  static final WsdlName wn = new WsdlName()

  final WsdlPluginExtension extension = project.wsdl
  
  @Input
  String projectName

  @Input
  File wsdlDirectory

  String wsdlName

  @TaskAction
  void findWsdlName() { 
    log.debug("finding the wsdl name")

    wsdlName = wn.findWsdlFileName(getProjectName())

    log.debug("checking that the file {} exists at {}", wsdlName, getWsdlDirectory())

    if (!new File(getWsdlDirectory(), "${wsdlName}.wsdl").exists()) {
      throw new GradleException("file ${wsdlName}.wsdl does not exist at default location of ${getWsdlDirectory()}")
    }

    extension.wsdlFileName = wsdlName
    extension.setWsdlPath()

    log.info("found wsdl Name: {} -- for this project", extension.wsdlPath)
  }
} 