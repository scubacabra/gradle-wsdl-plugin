package com.jacobo.gradle.plugins.tasks

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

  @TaskAction
  void start() { 
    log.info("finding the wsdl name")
    String name = wn.findWsdlFileName(project.name)
    log.debug("checking that the file {} exists at {}")
    if (!new File(wsdlDirectory, "${name}.wsdl").exists()) { // if file don't exist throw error
      throw new GradleException("file ${name}.wsdl does not exist at default location of ${project.extensions.wsdl.wsdlDirectory}")
    }
    project.extensions.wsdl.wsdlFileName = name
    log.info("found wsdl Name for this project")
  }
} 