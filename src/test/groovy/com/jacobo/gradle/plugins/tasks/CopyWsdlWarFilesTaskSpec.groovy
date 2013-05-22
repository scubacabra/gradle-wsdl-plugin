package com.jacobo.gradle.plugins.tasks

import com.jacobo.gradle.plugins.WsdlPlugin
import com.jacobo.gradle.plugins.model.GroupedWsdlWarFiles

import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project

import spock.lang.Specification

class CopyWsdlWarFilesTaskSpec extends Specification {
  
  private Project project
  private CopyWsdlWarFilesTask task
  
  def setup() {
    project = ProjectBuilder.builder().build()
    project.apply(plugin: "wsdl")
    task = project.tasks[WsdlPlugin.WSDL_PLUGIN_COPY_WSDL_WAR_FILES_TASK] as CopyWsdlWarFilesTask
  }

  def teardown() {
    
  }

  def "copy war files out" () {
  setup:
  def grouped1 = new GroupedWsdlWarFiles(groupedFolder: wsdlLocale, groupedFiles: ["abstract.wsdl", "noXsdImport.wsdl"])
  def grouped2 = new GroupedWsdlWarFiles(groupedFolder: schemaLocaleInclude, groupedFiles: ["include.xsd", "include2.xsd", "Product.xsd"])
  def grouped3 = new GroupedWsdlWarFiles(groupedFolder: schemaLocalePO, groupedFiles: ["PurchaseOrder.xsd"])
  def grouped4 = new GroupedWsdlWarFiles(groupedFolder: schemaLocaleMessages, groupedFiles: ["Messages.xsd"])
  def groupedWar = [grouped1, grouped2, grouped3, grouped4]

  and:
  def buildDir = new File(this.getClass().getResource("/wsdl").toURI()).parentFile.absoluteFile
  task.resolvedWebServicesDir = new File(buildDir, "web-service")
  task.resolvedWsdlDir = new File(task.resolvedWebServicesDir, "wsdl")
  task.resolvedSchemaDir = new File(task.resolvedWebServicesDir, "schema")
  task.rootDir = buildDir
  task.warFiles = groupedWar

  when:
  task.copyWarFilesToOutputDir()

  then:
  task.resolvedSchemaDir.exists()
  task.resolvedWsdlDir.exists()
  task.resolvedSchemaDir.exists()
  
  where:
  wsdlLocale = new File(this.getClass().getResource("/wsdl").toURI())
  schemaLocaleInclude = new File(this.getClass().getResource("/schema/Include").toURI())
  schemaLocalePO = new File(this.getClass().getResource("/schema/PO").toURI())
  schemaLocaleMessages = new File(this.getClass().getResource("/schema/Messages").toURI())
  }
}