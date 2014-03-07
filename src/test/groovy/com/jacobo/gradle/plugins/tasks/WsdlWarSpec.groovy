package com.jacobo.gradle.plugins.tasks

import groovy.util.AntBuilder

import org.gradle.api.plugins.WarPlugin

import com.jacobo.gradle.plugins.ProjectTaskSpecification
import com.jacobo.gradle.plugins.ant.AntExecutor
import com.jacobo.gradle.plugins.convert.NameToFileConverter
import com.jacobo.gradle.plugins.resolve.DependencyResolver
import com.jacobo.gradle.plugins.resolve.ChildrenPathResolver

class WsdlWarSpec extends ProjectTaskSpecification {
  
  def nameConverter = Mock(NameToFileConverter)
  def antExecutor = Mock(AntExecutor)
  def depResolver = Mock(DependencyResolver)
  def pathResolver = Mock(ChildrenPathResolver)
  def someWsdl = new File("some-wsdl.wsdl")
  
  def setup() {
    buildProject("wsdl")
    task = project.tasks[WarPlugin.WAR_TASK_NAME] as WsdlWar
    task.with { 
      converter = nameConverter
      dependencyResolver = depResolver
      childrenPathResolver = pathResolver
      copy = antExecutor
    }
  }

  def "war up this wsdl project"() {
    given:
    def wsdlDirectory = new File(project.rootDir, project.wsdl.wsdlFolder)
    def schemaDirectory = new File(project.rootDir, project.wsdl.schemaFolder)
    def resolvedDependencies = [] as Set

    when:
    task.start()

    then:
    1 * nameConverter.convert(project.name,
			      new File(project.rootDir,
				       project.wsdl.wsdlFolder)) >> someWsdl
    1 * depResolver.resolveDependencies(someWsdl) >> resolvedDependencies
    1 * pathResolver.resolvePaths(wsdlDirectory, resolvedDependencies) >> []
    1 * pathResolver.resolvePaths(schemaDirectory, resolvedDependencies) >> []
    1 * antExecutor.execute(_ as AntBuilder, ["toDir": new File(new File(project.buildDir, project.wsdl.webServiceCopyDir), project.wsdl.wsdlFolder),
    					      "fromDir": wsdlDirectory,
    					      "includeFiles": []])
    1 * antExecutor.execute(_ as AntBuilder, ["toDir": new File(new File(project.buildDir, project.wsdl.webServiceCopyDir), project.wsdl.schemaFolder),
    					      "fromDir": schemaDirectory,
    					      "includeFiles": []])
  }
}