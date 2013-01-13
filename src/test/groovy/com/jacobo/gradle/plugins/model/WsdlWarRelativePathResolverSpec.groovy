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

  def "resolve correctly" () { 
  when:
  def rootDir = new File(this.getClass().getResource("/wsdl/").toURI()).parentFile
  def result = relResolver.resolveRelativePathsToWar(rootDir, new File(file.toURI()))

  then:
  result.from == from
  result.into == into
  result.include == include

  where:
  file                                                         | from              | into              | include
  this.getClass().getResource("/wsdl/abstract.wsdl")           | new File("build/resources/test/wsdl").absoluteFile.path            | "wsdl"            | "abstract.wsdl"
  this.getClass().getResource("/wsdl/noXsdImport.wsdl")        | new File("build/resources/test/wsdl").absoluteFile.path            | "wsdl"            | "noXsdImport.wsdl"
  this.getClass().getResource("/schema/Messages/Messages.xsd") | new File("build/resources/test/schema/Messages").absoluteFile.path | "schema/Messages" | "Messages.xsd"
  this.getClass().getResource("/schema/PO/PurchaseOrder.xsd")  | new File("build/resources/test/schema/PO").absoluteFile.path       | "schema/PO"       | "PurchaseOrder.xsd"
  this.getClass().getResource("/schema/Include/include.xsd")   | new File("build/resources/test/schema/Include").absoluteFile.path  | "schema/Include"  | "include.xsd"
  
  }
}