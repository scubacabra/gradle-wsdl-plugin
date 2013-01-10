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
  this.getClass().getResource("/wsdl/abstract.wsdl")           | "wsdl"            | "wsdl"            | "abstract.wsdl" 
  this.getClass().getResource("/wsdl/noXsdImport.wsdl")        | "wsdl"            | "wsdl"            | "noXsdImport.wsdl"
  this.getClass().getResource("/schema/Messages/Messages.xsd") | "schema/Messages" | "schema/Messages" | "Messages.xsd"
  this.getClass().getResource("/schema/PO/PurchaseOrder.xsd")  | "schema/PO"       | "schema/PO"       | "PurchaseOrder.xsd"
  this.getClass().getResource("/schema/Include/include.xsd")   | "schema/Include"  | "schema/Include"  | "include.xsd"
  
  }
}