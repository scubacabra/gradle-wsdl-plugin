package com.jacobo.gradle.plugins.convert

import org.gradle.api.GradleException

import spock.lang.Specification
import spock.lang.Unroll

class WsdlNameConverterSpec extends Specification {
  
  def wsdlConverter = new WsdlNameConverter()
  
  @Unroll
  def "project name '#projectName' is converted to wsdl name '#result'" () {
    expect:
    result == wsdlConverter.convert(projectName)

    where:
    projectName            || result
    "spock-star-trek-ws"   || "SpockStarTrekService" 
    "srv-legend-ws"        || "SrvLegendService" 
    "boy-band-ws"          || "BoyBandService" 
  }

  @Unroll
  def "project name '#projectName' is lacking -ws suffix, cannot be converted" () { 
    when:
    result = wsdlConverter.convert(projectName)

    then:
    thrown(GradleException)
  
    where:
    projectName << ["spock-star-trek", "srv-legend", "boy-band"]
  }
}