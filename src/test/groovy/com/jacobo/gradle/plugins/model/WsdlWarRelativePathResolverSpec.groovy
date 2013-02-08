package com.jacobo.gradle.plugins.model

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

import spock.lang.Specification

/**
 * @author djmijares
 * Created: Mon Jan 07 18:08:42 EST 2013
 */
class WsdlWarRelativePathResolverSpec extends Specification { 

  def WsdlWarRelativePathResolver relResolver = new WsdlWarRelativePathResolver()

  def rootDir

  def setup() {
    //for these tests, always, build/resources/test/ root folder as rootDir, simulates project.rootDir
    rootDir = new File(this.getClass().getResource("/wsdl/").toURI()).parentFile
  }

  def "resolve correctly" () { 
  when:
  def result = relResolver.resolveRelativePathsToWar(rootDir, [new File(file.toURI())])

  then:
  result.resolvedFiles[0] == [new File(file.toURI())]
  result.into == [into]
  result.from == [from]

  where:
  file                                                         | from              | into              | include
  this.getClass().getResource("/wsdl/abstract.wsdl")           | new File("build/resources/test/wsdl").absoluteFile.path            | "wsdl"            | "abstract.wsdl"
  this.getClass().getResource("/wsdl/noXsdImport.wsdl")        | new File("build/resources/test/wsdl").absoluteFile.path            | "wsdl"            | "noXsdImport.wsdl"
  this.getClass().getResource("/schema/Messages/Messages.xsd") | new File("build/resources/test/schema/Messages").absoluteFile.path | "schema/Messages" | "Messages.xsd"
  this.getClass().getResource("/schema/PO/PurchaseOrder.xsd")  | new File("build/resources/test/schema/PO").absoluteFile.path       | "schema/PO"       | "PurchaseOrder.xsd"
  this.getClass().getResource("/schema/Include/include.xsd")   | new File("build/resources/test/schema/Include").absoluteFile.path  | "schema/Include"  | "include.xsd"
  
  }

  def "resolve two files into the directory as one object, not two" () {
  when: "setting up the instance variables"
    def result = relResolver.resolveRelativePathsToWar(rootDir, fileArray)

  then: "the result will return an array with one object"
    result.resolvedFiles == [fileArray]
    result.into == ["wsdl"]
    result.from == [new File("build/resources/test/wsdl").absoluteFile.path]

  where: "the array is defined as"
    fileArray = [this.getClass().getResource("/wsdl/abstract.wsdl"),  this.getClass().getResource("/wsdl/noXsdImport.wsdl")].collect { new File(it.toURI()) }    
  }
}