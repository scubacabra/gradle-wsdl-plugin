package org.gradle.jacobo.plugins.converter

import org.gradle.api.GradleException

import spock.lang.Specification
import spock.lang.Unroll

import org.gradle.jacobo.plugins.BaseSpecification

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
  def "apply name rule '#abbreviation' -> '#expansion' on '#projectName' -- '#result'" () {
    given:
    def nameRules = [(abbreviation) : expansion]

    expect:
    result == converter.convert(projectName, nameRules)

    where:
    abbreviation | expansion	    | projectName	      || result
    "-dm"	 | "DataManagement" | "spock-star-trek-dm-ws" || "SpockStarTrekDataManagementService"
    "-tj"	 | "TraderJoes"	    | "srv-legend-tj-ws"      || "SrvLegendTraderJoesService"
    "-wf"	 | "WholeFoods"	    | "boy-band-wf-ws"	      || "BoyBandWholeFoodsService"
  }

  @Unroll
  def "apply name rules '#nameRules' on '#projectName' -- '#result'" () {
    expect:
    result == converter.convert(projectName, nameRules)

    where:
    projectName			  || result
    "spock-tj-star-trek-dm-wf-ws" || "SpockTraderJoesStarTrekDataManagementWholeFoodsService"
    "spock-star-trek-dm-ty-ws"	  || "SpockStarTrekDataManagementTyService"
    "spock-star-trek-ws"	  || "SpockStarTrekService"
    "whole-foods-tj-ws"		  || "WholeFoodsTraderJoesService"
    nameRules = ["-dm": "DataManagement", "-tj" : "TraderJoes", "-wf" : "WholeFoods"]
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
    def result = converter.convert('existing-ws', wsdlDir, [:])

    then:
    result == getFileFromResourcePath("/wsdl/ExistingService.wsdl")
  }

  def "convert project name to non existent wsdl file"() { 
    given:
    def wsdlDir = getFileFromResourcePath("/wsdl")

    when:
    def result = converter.convert('non-existing-ws', wsdlDir, [:])

    then:
    thrown(GradleException)
  }
}