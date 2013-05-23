package com.jacobo.gradle.plugins.tasks

import com.jacobo.gradle.plugins.WsdlPlugin
import com.jacobo.gradle.plugins.model.GroupedWsdlWarFiles

import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project

import spock.lang.Specification

class CopyWsdlWarFilesTaskSpec extends Specification {
  
  private Project project
  private CopyWsdlWarFilesTask task
  private groupedWarFiles
  private rootDir
  private webServicesOutDir
  private wsdlFilesOutDir
  private schemaFilesOutDir
  
  def setup() {
    project = ProjectBuilder.builder().build()
    project.apply(plugin: "wsdl")
    task = project.tasks[WsdlPlugin.WSDL_PLUGIN_COPY_WSDL_WAR_FILES_TASK] as CopyWsdlWarFilesTask

    rootDir = new File(this.getClass().getResource("/wsdl").toURI()).parentFile.absoluteFile    
    webServicesOutDir = new File(rootDir, "web-service")
    wsdlFilesOutDir = new File(webServicesOutDir, "wsdl")
    schemaFilesOutDir = new File(webServicesOutDir, "schema")

    def grouped1 = new GroupedWsdlWarFiles(groupedFolder: new File(this.getClass().getResource("/wsdl").toURI()), groupedFiles: ["abstract.wsdl", "noXsdImport.wsdl"])
    def grouped2 = new GroupedWsdlWarFiles(groupedFolder: new File(this.getClass().getResource("/schema/Include").toURI()), groupedFiles: ["include.xsd", "include2.xsd", "Product.xsd"])
    def grouped3 = new GroupedWsdlWarFiles(groupedFolder: new File(this.getClass().getResource("/schema/PO").toURI()), groupedFiles: ["PurchaseOrder.xsd"])
    def grouped4 = new GroupedWsdlWarFiles(groupedFolder: new File(this.getClass().getResource("/schema/Messages").toURI()), groupedFiles: ["Messages.xsd"])
    groupedWarFiles = [grouped1, grouped2, grouped3, grouped4]    
  }

  def teardown() {
    new AntBuilder().delete(webServicesOutDir.canonicalPath)
  }

  def "copy war files out" () {
  given:
  task.resolvedWebServicesDir = webServicesOutDir
  task.resolvedWsdlDir = wsdlFilesOutDir
  task.resolvedSchemaDir = schemaFilesOutDir
  task.rootDir = rootDir
  task.warFiles = groupedWarFiles

  when:
  task.copyWarFilesToOutputDir()
  def wsdlFiles = wsdlFilesOutDir.listFiles()
  def schemaFolders = schemaFilesOutDir.listFiles()
  def includeFiles = new File(schemaFilesOutDir, "Include").listFiles()
  def POFiles = new File(schemaFilesOutDir, "PO").listFiles()
  def MessageFiles = new File(schemaFilesOutDir, "Messages").listFiles()
  
  then:
  task.resolvedWebServicesDir.exists()
  task.resolvedWsdlDir.exists()
  task.resolvedSchemaDir.exists()
  wsdlFiles.length == 2
  wsdlFiles[0].name == "abstract.wsdl"
  wsdlFiles[1].name == "noXsdImport.wsdl"
  schemaFolders.length == 3
  schemaFolders[0].name == "Include"
  schemaFolders[1].name == "Messages"
  schemaFolders[2].name == "PO"
  includeFiles.length == 3
  includeFiles[0].name == "include.xsd"
  includeFiles[1].name == "include2.xsd"
  includeFiles[2].name == "Product.xsd"
  POFiles.length == 1
  POFiles[0].name == "PurchaseOrder.xsd"
  MessageFiles.length == 1
  MessageFiles[0].name == "Messages.xsd"
  }
}