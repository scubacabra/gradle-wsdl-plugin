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
  def WsdlNameHelper wn = new WsdlNameHelper()

  def "append 'Service' to the already CamelCased String" () { 
  expect:
  result == wn.appendService(camelCase)

  where:
  camelCase       | result
  "SpockStarTrek" | "SpockStarTrekService"
  "SRVLegend"     | "SRVLegendService"
  "BoyBand"       | "BoyBandService"
  }

  def "convert dashed string to appropriate camel case" () { 
  expect:
  result == wn.convertDashedToCamelCase(dashed)

  where:
  dashed            | result
  "spock-star-trek" |  "spockStarTrek"
  "srv-legend"      |  "srvLegend"
  "boy-band"        |  "boyBand"
  }

  def "remove ws suffix at the end of the project name" () { 
  expect:
  result == wn.removeSuffix(projectName)

  where:
  projectName            | result
  "spock-star-trek-ws"   |  "spock-star-trek" 
  "srv-legend-ws"        |  "srv-legend"      
  "boy-band-ws"          |  "boy-band"        
  }

  def "capitalize First Letter of Name" () { 
  expect:
  result == wn.capitalizeFirstLetter(capitalize)

  where:
  capitalize            | result
  "spockStarTrek"       | "SpockStarTrek"
  "srvLegend"           | "SrvLegend"
  "boyBand"             | "BoyBand"
  }

  def "test full cycle, including errors" () { //TODO put some none ending -ws projectName to throw error
  expect:
  result == wn.findWsdlFileName(projectName)
  wn.wsdlName == result

  where:
  projectName            | result
  "spock-star-trek-ws"   | "SpockStarTrekService" 
  "srv-legend-ws"        | "SrvLegendService" 
  "boy-band-ws"          | "BoyBandService" 
  }

  def "project name is lacking -ws suffix" () { 
  when:
  wn.findWsdlFileName(projectName)

  then:
  def ex =  thrown(GradleException)
  ex.message == "${projectName} is not conforming to the convention, needs to be suffixed with '-ws' at the end at the very least.  Double check"
  
  where:
  projectName << ["spock-star-trek", "srv-legend", "boy-band"]

  }
}