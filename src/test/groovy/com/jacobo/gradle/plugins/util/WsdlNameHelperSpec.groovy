package com.jacobo.gradle.plugins.util

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

import spock.lang.Specification

import org.gradle.api.GradleException

/**
 * @author djmijares
 * Created: Mon Jan 07 18:08:42 EST 2013
 */
class WsdlNameHelperSpec extends Specification {

  def "append 'Service' to the already CamelCased String" () { 
  expect:
  result == WsdlNameHelper.appendService(camelCase)

  where:
  camelCase       | result
  "SpockStarTrek" | "SpockStarTrekService"
  "SRVLegend"     | "SRVLegendService"
  "BoyBand"       | "BoyBandService"
  }

  def "convert dashed string to appropriate camel case" () { 
  expect:
  result == WsdlNameHelper.convertDashedToCamelCase(dashed)

  where:
  dashed            | result
  "spock-star-trek" |  "spockStarTrek"
  "srv-legend"      |  "srvLegend"
  "boy-band"        |  "boyBand"
  }

  def "remove ws suffix at the end of the project name" () { 
  expect:
  result == WsdlNameHelper.removeSuffix(projectName)

  where:
  projectName            | result
  "spock-star-trek-ws"   |  "spock-star-trek" 
  "srv-legend-ws"        |  "srv-legend"      
  "boy-band-ws"          |  "boy-band"        
  }

  def "capitalize First Letter of Name" () { 
  expect:
  result == WsdlNameHelper.capitalizeFirstLetter(capitalize)

  where:
  capitalize            | result
  "spockStarTrek"       | "SpockStarTrek"
  "srvLegend"           | "SrvLegend"
  "boyBand"             | "BoyBand"
  }

  def "test full cycle, including errors" () {
  expect:
  result == WsdlNameHelper.generateWsdlName(projectName)

  where:
  projectName            | result
  "spock-star-trek-ws"   | "SpockStarTrekService" 
  "srv-legend-ws"        | "SrvLegendService" 
  "boy-band-ws"          | "BoyBandService" 
  }

  def "project name is lacking -ws suffix" () { 
  when:
  result = WsdlNameHelper.generateWsdlName(projectName)

  then:
  def ex =  thrown(GradleException)
  ex.message == "${projectName} is not conforming to the convention, needs to be suffixed with '-ws' at the end at the very least.  Double check"
  
  where:
  projectName << ["spock-star-trek", "srv-legend", "boy-band"]
  }

  def "test special abbreviation designations" () { //TODO put some none ending -ws projectName to throw error
  expect:
  result == wn.findWsdlFileName(projectName)

  where:
  abbreviation  | projectName            | result
  "dm" | "spock-star-trek-dm-ws"   | "SpockStarTrekDataManagementService" 
  "tj" | "srv-legend-tj-ws"        | "SrvLegendTraderJoesService" 
  "wf" | "boy-band-wf-ws"          | "BoyBandWholeFoodsService"
  ""   | "boy-band-ws"             | "BoyBandService"
  }
}