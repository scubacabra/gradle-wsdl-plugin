package com.jacobo.gradle.plugins.tasks

import com.jacobo.gradle.plugins.ProjectTaskSpecification
import com.jacobo.gradle.plugins.WsdlPlugin
import com.jacobo.gradle.plugins.resolve.WsdlDependencyResolver

class WsdlResolveDependenciesSpec extends ProjectTaskSpecification {
  
  def depResolver = Mock(WsdlDependencyResolver)
  def projectDir = fakeProjectDir("fake-project")
  def someWsdl = new File("some-wsdl.wsdl")
  // returned dependencies
  def dependencies = ["some-wsdl.wsdl", "dep1.xsd", "dep2.xsd"].collect {
    new File(projectDir, it)
  } as Set
  
  def setup() {
    projectAtDir(projectDir, "wsdl")
    task = project.tasks[WsdlPlugin.RESOLVE_DEPENDENCIES_TASK_NAME] as WsdlResolveDependencies
    task.with {
      wsdlFile = someWsdl
      dependencyResolver = depResolver
    }
  }

  def cleanup() {
    projectDir.deleteDir()
  }

  def "resolve dependencies for #someWsdl"() {
    when:
    task.start()

    then:
    1 * depResolver.resolveDependencies(someWsdl) >> dependencies
    project.wsdl.wsdlDependencies.files.size() == dependencies.size()
    project.wsdl.wsdlDependencies.files == dependencies
  }
}