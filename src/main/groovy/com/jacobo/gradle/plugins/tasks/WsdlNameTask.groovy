package com.jacobo.gradle.plugins.tasks

import com.jacobo.gradle.plugins.WsdlExtension

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

import org.gradle.api.tasks.TaskAction
import org.gradle.api.DefaultTask

import org.gradle.api.GradleException

import com.jacobo.gradle.plugins.model.WsdlName

/**
 * @author djmijares
 * Created: Mon Jan 07 18:08:42 EST 2013
 */
class WsdlNameTask extends DefaultTask { 
  static final Logger log = Logging.getLogger(WsdlNameTask.class)
  static final WsdlName wn = new WsdlName()

  final WsdlExtension extension = project.wsdl

  @TaskAction
  void start() { 
    log.info("finding the wsdl name")
    String name = wn.findWsdlFileName(project.name)
    log.debug("checking that the file {} exists at {}", name, extension.wsdlDirectory)
    if (!new File(extension.wsdlDirectory, "${name}.wsdl").exists()) { // if file don't exist throw error
      throw new GradleException("file ${name}.wsdl does not exist at default location of ${extension.wsdlDirectory}")
    }
    extension.wsdlFileName = name
    extension.setWsdlPath()
    project.wsdl.wsdlPath = new File(extension.wsdlDirectory, extension.wsdlFileName + ".wsdl")
    log.info("found wsdl Name: {} -- for this project", project.wsdl.wsdlPath)
    log.info("found wsdl Name: {} -- for this project", extension.wsdlPath)
  }
} 