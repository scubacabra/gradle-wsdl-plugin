package org.gradle.jacobo.plugins.task

import groovy.util.AntBuilder

import org.gradle.api.file.FileCollection

import spock.lang.Unroll

import org.gradle.jacobo.plugins.ProjectTaskSpecification
import org.gradle.jacobo.plugins.WsdlPlugin
import org.gradle.jacobo.plugins.ant.AntExecutor
import org.gradle.jacobo.plugins.converter.NameToFileConverter

class WsdlWsImportSpec extends ProjectTaskSpecification {

  def executor = Mock(AntExecutor)
  def someWsdl = new File("some-wsdl.wsdl")
  def projectDir = getFileFromResourcePath("/fake-project")
  
  def setup() {
    projectAtDir(projectDir, "com.github.jacobono.wsdl")
    task = project.tasks[WsdlPlugin.WSIMPORT_TASK_NAME] as WsdlWsImport
  }

  @Unroll
  def "execute wsimport on the wsdl file, binding episode files '#episodeFiles'"() {
    given: "set up the classpath and task variables"
    def classpath = project.configurations[WsdlPlugin.WSDL_CONFIGURATION_NAME]
    task.with { 
      antExecutor = executor
      wsdlFile = someWsdl
    }

    and: "the episode directory and files to bind are"
    project.wsdl.episodeFolder = "episodes"
    project.wsdl.episodes = episodeFiles
    
    and: "output directory is"
    def destinationDir = new File(project.projectDir, "src/main/java")

    and: "the set of episode files is"
    def episodes = project.files(episodeFiles.collect { new File(project.rootDir, "${project.wsdl.episodeFolder}/${it}") })

    when:
    task.start()

    then:
    1 * executor.execute(_ as AntBuilder, someWsdl, project.wsdl.wsimport,
			 destinationDir, classpath.asPath,
			 { it.files == episodes.files })
    where:
    episodeFiles << [[],
		     ["fake-episode.episode", "another-fake-episode.episode"]]
  }
}