package com.jacobo.gradle.plugins.tasks

import spock.lang.Unroll

import com.jacobo.gradle.plugins.ProjectIntegrationSpec

class ResolveWsdlDependenciesSpecification extends ProjectIntegrationSpec {

  def setup() {
    def rootDir = getFileFromResourcePath("/test-wsdl-project")
    setRootProject(rootDir)
  }

  @Unroll
  def "resolve wsdl dependencies of '#projectName'"() {
    given: "setup sub project and tasks"
    setSubProject(rootProject, projectName, "wsdl")
    setupProjectTasks()

    // simulate what gradle would do here, dependent Tasks need to run first    
    and: "dependent task is executed"
    convertTask.execute()

    when: "resolve task is executed"
    resolveTask.execute()

    then: "these are the wsdl dependencies"
    project.wsdl.wsdlDependencies.files == project.files(
      ["/test-wsdl-project/wsdl/IntegrationTestService.wsdl",
       "/test-wsdl-project/schema/Messages/Messages.xsd",
       "/test-wsdl-project/schema/PO/PurchaseOrder.xsd"
      ].collect{ getFileFromResourcePath(it) }
    ).files
    
    where:
    projectName = "integration-test-ws"
  }
}