package com.jacobo.gradle.plugins.model

import spock.lang.Specification

class WsdlSlurperSpec extends Specification {
  
  def slurper = new WsdlSlurper()
  
  def "wsdl dependency locations" () {
  setup:
  def slurped = new XmlSlurper().parse(new File(file.toURI()))

  when:
  slurper.processWsdlDependencyLocations(slurped.import)

  then:
  slurper.wsdlImports == imports

  when:
  def result = slurper.gatherAllRelativeLocations()

  then:
  result == imports

  where:
  file | imports
  this.getClass().getResource("/wsdl/noXsdImport.wsdl") | []
  this.getClass().getResource("/wsdl/ImportsAnotherWsdl.wsdl") | ["./abstract.wsdl"]
  }

  
}