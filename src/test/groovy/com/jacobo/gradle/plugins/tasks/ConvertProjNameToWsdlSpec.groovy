package com.jacobo.gradle.plugins.tasks

import com.jacobo.gradle.plugins.ProjectTaskSpecification

import com.jacobo.gradle.plugins.WsdlPlugin
import com.jacobo.gradle.plugins.convert.NameToFileConverter

class ConvertProjNameToWsdlSpec extends ProjectTaskSpecification {

  def nameConverter = Mock(NameToFileConverter)
  def wsdlFile = new File("wsdlDir/some-wsdl.wsdl")

  def setup() {
    buildProject("wsdl")
    task = project.tasks[WsdlPlugin.CONVERSION_TASK_NAME] as ConvertProjNameToWsdl
    task.with {
      converter = nameConverter
      wsdlDirectory = new File("wsdlDir")
      projectName = project.name
    }
  }

  def "convert project name to wsdl File"() {
    when:
    task.start()

    then:
    1 * nameConverter.convert(project.name, new File("wsdlDir")) >> wsdlFile
    project.wsdl.wsdlFile == wsdlFile
  }
}