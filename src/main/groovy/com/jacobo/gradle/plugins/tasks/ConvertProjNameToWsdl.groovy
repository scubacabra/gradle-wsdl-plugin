package com.jacobo.gradle.plugins.tasks

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputFile

import com.jacobo.gradle.plugins.convert.NameToFileConverter

class ConvertProjNameToWsdl extends DefaultTask { 
  static final Logger log = Logging.getLogger(ConvertProjNameToWsdl.class)

  String projectName

  File wsdlDirectory

  /**
   * project name to wsdl file converter service
   */
  NameToFileConverter converter

  @TaskAction
  void start() {
    def projectName = getProjectName()
    def wsdlDir = getWsdlDirectory()
    def nameConverter = getConverter()
    log.info("looking for wsdl in directory '{}'", wsdlDir)
    def wsdlFile = nameConverter.convert(projectName, wsdlDir)
    log.info("converted '{}' to wsdl file '{}'", projectName, wsdlFile)
    project.wsdl.wsdlFile = wsdlFile
  }
}