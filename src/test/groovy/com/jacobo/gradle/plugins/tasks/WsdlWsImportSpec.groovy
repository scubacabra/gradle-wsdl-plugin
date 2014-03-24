package com.jacobo.gradle.plugins.tasks

import groovy.util.AntBuilder

import org.gradle.api.file.FileCollection

import com.jacobo.gradle.plugins.ProjectTaskSpecification
import com.jacobo.gradle.plugins.WsdlPlugin
import com.jacobo.gradle.plugins.ant.AntExecutor
import com.jacobo.gradle.plugins.convert.NameToFileConverter

class WsdlWsImportSpec extends ProjectTaskSpecification {

  def nameConverter = Mock(NameToFileConverter)
  def executor = Mock(AntExecutor)
  def someWsdl = new File("some-wsdl.wsdl")
  
  def setup() {
    buildProject("wsdl")
    task = project.tasks[WsdlPlugin.WSIMPORT_TASK_NAME] as WsdlWsImport
  }

  def "execute wsimport on the wsdl file"() {
    given:
    def classpath = project.configurations[WsdlPlugin.WSDL_CONFIGURATION_NAME]
    task.with { 
      antExecutor = executor
      wsdlFile = someWsdl
    }

    and:
    def destinationDir = new File(project.projectDir, "src/main/java")

    when:
    task.start()

    then:
    1 * executor.execute(_ as AntBuilder, ["wsdl": someWsdl,
					   "extension": project.wsdl.wsimport,
					   "destinationDir": destinationDir,
					   "episodeFiles": [] as Set,
					   "classpath": classpath.asPath])
  }
}