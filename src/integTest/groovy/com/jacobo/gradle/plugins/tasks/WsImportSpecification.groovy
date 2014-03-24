package com.jacobo.gradle.plugins.tasks

import spock.lang.Unroll
import spock.lang.Ignore

import com.jacobo.gradle.plugins.ProjectIntegrationSpec

class WsImportSpecification extends ProjectIntegrationSpec {

  def setup() {
    def rootDir = getFileFromResourcePath("/test-wsdl-project")
    setRootProject(rootDir)
  }

  @Unroll
  @Ignore
  def "run task wsimport for project '#projectName'"() {
    given: "setup sub project and tasks"
    setSubProject(rootProject, projectName, "wsdl")
    setupProjectTasks()
    
    and: "set up project dependencies and repositories to download jars"
    project.dependencies { 
      jaxws 'com.sun.xml.ws:jaxws-tools:2.2.8'
      jaxws 'com.sun.xml.ws:jaxws-rt:2.2.8'
    }
    project.repositories { 
      mavenCentral()
    }

    // simulate what gradle would do here, dependent Tasks need to run first    
    and: "dependent tasks are executed"
    [convertTask, resolveTask].each { it.execute() }

    when: "ws import is executed"
    wsImportTask.execute()

    and: "src output directory is"
    def outputDir = getFileFromResourcePath("/test-wsdl-project/integration-test-ws/src/main/java")

    then: "these files should be in outputDir and an episode file is created"
    outputDir.exists()
    outputDir.isDirectory()
    
    where:
    projectName = "integration-test-ws"
  }
}