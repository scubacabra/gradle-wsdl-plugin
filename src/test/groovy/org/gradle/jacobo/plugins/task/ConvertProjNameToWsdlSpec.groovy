package org.gradle.jacobo.plugins.task

import org.gradle.jacobo.plugins.ProjectTaskSpecification

import org.gradle.jacobo.plugins.WsdlPlugin
import org.gradle.jacobo.plugins.converter.NameToFileConverter

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
    1 * nameConverter.convert(project.name, new File("wsdlDir"), [:]) >> wsdlFile
    project.wsdl.wsdlFile == wsdlFile
  }

  def "convert project name to wsdl File, with applied name rules"() {
    given: "set up project's name rules"
    def nameRules = ['rule1':'expansion1', 'rule2':'expansion2',
		     'rule3':'expansion3']
    project.wsdl.nameRules = nameRules

    when:
    task.start()

    then:
    1 * nameConverter.convert(project.name, new File("wsdlDir"),
			      nameRules) >> wsdlFile
    project.wsdl.wsdlFile == wsdlFile
  }
}