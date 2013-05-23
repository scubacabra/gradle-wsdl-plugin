package com.jacobo.gradle.plugins.util

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

import spock.lang.Specification

/**
 * @author djmijares
 * Created: Mon Jan 07 18:08:42 EST 2013
 */
class WsdlWarFileGrouperSpec extends Specification {

  def rootDir

  def setup() {
    //for these tests, always, build/resources/test/ root folder as rootDir, simulates project.rootDir
    rootDir = new File(this.getClass().getResource("/wsdl/").toURI()).parentFile
  }

  def "resolve correctly" () { 
  when:
  def result = WsdlWarFileGrouper.groupFilesWithCommonParentDirs([new File(file.toURI())])

  then:
  result[0].groupedFiles == [name]
  result[0].groupedFolder == new File(file.toURI()).parentFile

  where:
  file                                                         | name
  this.getClass().getResource("/wsdl/abstract.wsdl")           | "abstract.wsdl"
  this.getClass().getResource("/wsdl/noXsdImport.wsdl")        | "noXsdImport.wsdl"
  this.getClass().getResource("/schema/Messages/Messages.xsd") | "Messages.xsd"
  this.getClass().getResource("/schema/PO/PurchaseOrder.xsd")  | "PurchaseOrder.xsd"
  this.getClass().getResource("/schema/Include/include.xsd")   | "include.xsd"
  
  }

  def "resolve two files into the directory as one object, not two" () {
  when: "setting up the instance variables"
    def result = WsdlWarFileGrouper.groupFilesWithCommonParentDirs(fileArray)

  then: "the result will return an array with one object"
    result.groupedFiles == [fileArray*.name]
    result.groupedFolder == [new File(this.getClass().getResource("/wsdl").toURI())]

  where: "the array is defined as"
    fileArray = [this.getClass().getResource("/wsdl/abstract.wsdl"),  this.getClass().getResource("/wsdl/noXsdImport.wsdl")].collect { new File(it.toURI()) }    
  }
}