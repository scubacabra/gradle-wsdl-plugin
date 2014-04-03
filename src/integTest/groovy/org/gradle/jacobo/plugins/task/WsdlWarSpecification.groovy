package org.gradle.jacobo.plugins.task

import org.gradle.jacobo.plugins.ProjectIntegrationSpec

import spock.lang.Unroll

class WsdlWarSpecification extends ProjectIntegrationSpec {

  def setup() {
    def rootDir = getFileFromResourcePath("/test-wsdl-project")
    setRootProject(rootDir)
  }

  @Unroll
  def "run task wsimport for project '#projectName'"() {
    given: "setup sub project and tasks"
    setSubProject(rootProject, projectName, "wsdl")
    setupProjectTasks()

    // simulate what gradle would do here, dependent Tasks need to run first    
    and: "dependent tasks are executed"
    [convertTask, resolveTask].each { it.execute() }

    and: "resolved dependencies returns its FileCollection"
    def resolvedDependencies = project.files(
      "../wsdl/IntegrationTestService.wsdl",
      "../schema/Messages/Messages.xsd",
      "../schema/PO/PurchaseOrder.xsd"
    )

    when: "war task is executed"
    warTask.execute()

    and: "get a  ziptree of the war"
    def war = project.zipTree("build/libs/integration-test-ws.war")

    then: '''zip tree file names should contain all resolvedDependencies file
names (file names used because the unzipping of a war is in a tmp folder not 
in place)'''
    resolvedDependencies.files.each { file -> war.files.name.contains(file.name) }

    where:
    projectName = "integration-test-ws"
  }
}