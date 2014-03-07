package com.jacobo.gradle.plugins.tasks

import groovy.util.AntBuilder

import com.jacobo.gradle.plugins.WsdlPlugin
import com.jacobo.gradle.plugins.ProjectTaskSpecification
import com.jacobo.gradle.plugins.ant.AntExecutor
import com.jacobo.gradle.plugins.convert.NameToFileConverter

class WsdlWsImportSpec extends ProjectTaskSpecification {

  def nameConverter = Mock(NameToFileConverter)
  def antExecutor = Mock(AntExecutor)
  def someWsdl = new File("some-wsdl.wsdl")
  

  def setup() {
    buildProject("wsdl")
    task = project.tasks[WsdlPlugin.WSIMPORT_TASK_NAME] as WsdlWsImport
  }

  def "execute wsimport on the wsdl file"() {
    given:
    def classpath = project.configurations[WsdlPlugin.WSDL_CONFIGURATION_NAME]
    task.with { 
      converter = nameConverter
      wsimport = antExecutor
    }

    when:
    task.start()

    then:
    1 * nameConverter.convert(project.name,
			  new File(project.rootDir,
				   project.wsdl.episodeFolder)) >> someWsdl
    1 * antExecutor.execute(_ as AntBuilder, ["wsdl": someWsdl,
					      "extension": project.wsdl.wsimport,
					      "classpath": classpath.asPath])
  }
}