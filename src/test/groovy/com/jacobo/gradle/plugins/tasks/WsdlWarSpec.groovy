package com.jacobo.gradle.plugins.tasks

import org.gradle.api.file.FileCollection
import org.gradle.api.plugins.WarPlugin

import com.jacobo.gradle.plugins.ProjectTaskSpecification

class WsdlWarSpec extends ProjectTaskSpecification {
  
  def someWsdl = new File("some-wsdl.wsdl")
  
  def setup() {
    buildProject("wsdl")
    task = project.tasks[WarPlugin.WAR_TASK_NAME] as WsdlWar
    task.with {
      wsdlDependencies = project.files([new File("some-wsdl.wsdl"),
					new File("some-xsd.xsd")])
    }
  }

  // not a whole lot to test besides the inputFiles
  def "war up this wsdl project"() {
    expect:
    task.wsdlDependencies != null
    task.wsdlDependencies instanceof FileCollection
  }
}