package org.gradle.jacobo.plugins.task

import org.gradle.api.DefaultTask
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.TaskAction

import org.gradle.jacobo.plugins.converter.NameToFileConverter

class ConvertProjNameToWsdl extends DefaultTask { 
  static final Logger log = Logging.getLogger(ConvertProjNameToWsdl.class)

  String projectName

  File wsdlDirectory

  Map<String, String> nameRules
  
  /**
   * project name to wsdl file converter service
   */
  NameToFileConverter converter

  @TaskAction
  void start() {
    def projectName = getProjectName()
    def wsdlDir = getWsdlDirectory()
    def nameRules = getNameRules()
    log.info("looking for wsdl in directory '{}'", wsdlDir)
    def wsdlFile = getConverter().convert(projectName, wsdlDir, nameRules)
    log.info("converted '{}' to wsdl file '{}'", projectName, wsdlFile)
    project.wsdl.wsdlFile = wsdlFile
  }
}