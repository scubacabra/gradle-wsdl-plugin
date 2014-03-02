package com.jacobo.gradle.plugins.convert

import org.gradle.api.GradleException

import spock.lang.Specification
import spock.lang.Unroll

import com.jacobo.gradle.plugins.BaseSpecification

class ProjectToWsdlFileConverterSpec extends BaseSpecification {
  
  def converter = new ProjectToWsdlFileConverter()
  
  @Unroll
  def "project name '#projectName' is converted to wsdl name '#result'" () {
    expect:
    result == converter.convert(projectName)

    where:
    projectName            || result
    "spock-star-trek-ws"   || "SpockStarTrekService" 
    "srv-legend-ws"        || "SrvLegendService" 
    "boy-band-ws"          || "BoyBandService" 
  }

  @Unroll
  def "project name '#projectName' is lacking -ws suffix, cannot be converted" () { 
    when:
    result = converter.convert(projectName)

    then:
    thrown(GradleException)
  
    where:
    projectName << ["spock-star-trek", "srv-legend", "boy-band"]
  }

  def "convert project name to existing wsdl file"() { 
    given:
    def wsdlDir = getFileFromResourcePath("/wsdl")

    when:
    def result = converter.convert('existing-ws', wsdlDir)

    then:
    result == getFileFromResourcePath("/wsdl/ExistingService.wsdl")
  }

  def "convert project name to non existent wsdl file"() { 
    given:
    def wsdlDir = getFileFromResourcePath("/wsdl")

    when:
    def result = converter.convert('non-existing-ws', wsdlDir)

    then:
    thrown(GradleException)
  }
}