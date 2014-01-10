package com.jacobo.gradle.plugins.tasks

import com.jacobo.gradle.plugins.ProjectTaskSpecification
import com.jacobo.gradle.plugins.tasks.GroupWarFiles
import com.jacobo.gradle.plugins.WsdlPlugin

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

/**
 * @author jacobono
 * Created: Mon Jan 07 18:08:42 EST 2013
 */
class GroupWarFilesSpec extends ProjectTaskSpecification {

  def setup() { 
    task = project.tasks[WsdlPlugin.WSDL_PLUGIN_GROUP_WAR_FILES_TASK] as GroupWarFiles
  }

  def "Group Dependent War Files correctly when there is not a current grouping for them already" () { 
  when: "you have a task with these dependent files and you group them"
    task.with { 
      wsdlDependencies = dependentFiles
    }

    def result = task.groupWarFilesByCommonDirectories(dependentFiles)

  then: "You get one gropued object populated in the list"
    result[0].groupedFiles == groupedFileNames
    result[0].groupedFolder == dependentFiles[0].parentFile

  where: "You have a list of dependent files and the files they should have under them"
    dependentFiles                                             | groupedFileNames
    [getFileFromResourcePath("/wsdl/abstract.wsdl")]           | ["abstract.wsdl"]
    [getFileFromResourcePath("/wsdl/noXsdImport.wsdl")]        | ["noXsdImport.wsdl"]
    [getFileFromResourcePath("/schema/Messages/Messages.xsd")] | ["Messages.xsd"]
    [getFileFromResourcePath("/schema/PO/PurchaseOrder.xsd")]  | ["PurchaseOrder.xsd"]
    [getFileFromResourcePath("/schema/Include/include.xsd")]   | ["include.xsd"]
  
  }

  def "Resolve two files into the directory as one object, not two" () {
  when: "setting up the instance variables"
    task.with { 
      wsdlDependencies = fileArray
    }
    def result = task.groupWarFilesByCommonDirectories(fileArray)

  then: "the result will return an array with one object"
    result.groupedFiles == [fileArray*.name]
    result.groupedFolder == [getFileFromResourcePath("/wsdl")]

  where: "the array is defined as"
    fileArray = ["/wsdl/abstract.wsdl",  "/wsdl/noXsdImport.wsdl"].collect{ getFileFromResourcePath(it) }
  }

  def "Resolve files spanning two different parent Directories. You end up with two GroupedWarFiles objects" () {
  when: "setting up the instance variables"
    task.with { 
      wsdlDependencies = fileArray
    }
    def result = task.groupWarFilesByCommonDirectories(fileArray)

  then: "the result will return an array with one object"
    result.size == 2
    result[0].groupedFiles == fileArray*.name.findAll{ it.contains("wsdl") == true }
    result[0].groupedFolder == getFileFromResourcePath("/wsdl")
    result[1].groupedFiles == fileArray*.name.findAll{ it.contains("xsd") == true }
    result[1].groupedFolder == getFileFromResourcePath("/schema/Messages")
  where: "the array is defined as"
    fileArray = ["/wsdl/abstract.wsdl",  "/wsdl/noXsdImport.wsdl", "/schema/Messages/Messages.xsd"].collect{ getFileFromResourcePath(it) }
  }
}