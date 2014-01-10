package com.jacobo.gradle.plugins.tasks

import com.jacobo.gradle.plugins.WsdlPlugin
import com.jacobo.gradle.plugins.model.GroupedWarFiles
import com.jacobo.gradle.plugins.tasks.CopyWarFiles

import com.jacobo.gradle.plugins.ProjectTaskSpecification

class CopyWarFilesSpec extends ProjectTaskSpecification {
  
  def groupedWarFiles
  def rootDir = getFileFromResourcePath("/wsdl/").parentFile.absoluteFile
  def webServicesOutDir = new File(rootDir, "web-service")
  def wsdlFilesOutDir = new File(webServicesOutDir, "wsdl")
  def schemaFilesOutDir = new File(webServicesOutDir, "schema")
  
  def setup() {
    task = project.tasks[WsdlPlugin.WSDL_PLUGIN_COPY_WAR_FILES_TASK] as CopyWarFiles

    def grouped1 = new GroupedWarFiles(groupedFolder: getFileFromResourcePath("/wsdl"), groupedFiles: ["abstract.wsdl", "noXsdImport.wsdl"])
    def grouped2 = new GroupedWarFiles(groupedFolder: getFileFromResourcePath("/schema/Include"), groupedFiles: ["include.xsd", "include2.xsd", "Product.xsd"])
    def grouped3 = new GroupedWarFiles(groupedFolder: getFileFromResourcePath("/schema/PO"), groupedFiles: ["PurchaseOrder.xsd"])
    def grouped4 = new GroupedWarFiles(groupedFolder: getFileFromResourcePath("/schema/Messages"), groupedFiles: ["Messages.xsd"])
    groupedWarFiles = [grouped1, grouped2, grouped3, grouped4]    
  }

  def teardown() {
    new AntBuilder().delete(webServicesOutDir.canonicalPath)
  }

  def "Copy Grouped War Files out to the webServicesOutDir" () {
  given: "a task with these parameters"
    task.with { 
      webServicesCopyDir	= webServicesOutDir
      projectRootDir		= rootDir
      warFiles			= groupedWarFiles
    }
  

  when: "copy war files to temporary output directory"
    task.copyWarFilesToOutputDir()
    def wsdlFiles = wsdlFilesOutDir.listFiles()
    def schemaFolders = schemaFilesOutDir.listFiles()
    def includeFiles = new File(schemaFilesOutDir, "Include").listFiles()
    def poFiles = new File(schemaFilesOutDir, "PO").listFiles()
    def messageFiles = new File(schemaFilesOutDir, "Messages").listFiles()
  
  then: "Each of the directories exist, because the ant task populated them"
    [task.webServicesCopyDir, new File(task.webServicesCopyDir, "wsdl"), new File(task.webServicesCopyDir, "schema")].each { it.exists() == true }
  

  then: "WSDL Files are copied correctly"
    wsdlFiles.length == 2
    ["abstract.wsdl", "noXsdImport.wsdl"].each { wsdlFiles.name.contains(it) }

  then: "XSD files are copied correctly"
    schemaFolders.length == 3
    ["Include", "Messages", "PO"].each { schemaFolders.name.contains(it) }
    includeFiles.length == 3
    ["include.xsd", "include2.xsd", "Product.xsd"].each { includeFiles.name.contains(it) }
    poFiles.length == 1
    poFiles.name.contains("PurchaseOrder.xsd")
    messageFiles.length == 1
    messageFiles.name.contains("Messages.xsd")
  }
}