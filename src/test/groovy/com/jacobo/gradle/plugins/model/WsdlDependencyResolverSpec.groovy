package com.jacobo.gradle.plugins.model

import com.jacobo.gradle.plugins.util.FileHelper

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

import spock.lang.Specification

/**
 * @author djmijares
 * Created: Mon Jan 07 18:08:42 EST 2013
 */
class WsdlDependencyResolverSpec extends Specification { 

  def WsdlDependencyResolver wdr = new WsdlDependencyResolver()

  def "add schema to Parse to list"() {   
  
  when:
  wdr.schemaLocationsToParse = [new File("test.wsdl")]
  wdr.addSchemaLocationToParse(file)

  then:
  result == wdr.schemaLocationsToParse

  where:
  file                    | result
  new File("schema.xsd")  | [new File("test.wsdl"), new File("schema.xsd")]
  new File("nothing.xsd") | [new File("test.wsdl"), new File("nothing.xsd")]
  new File("test.wsdl")   | [new File("test.wsdl")]

  }

  def "add absolute path dependencies" () { 

  when:
  wdr.absolutePathDependencies = [new File("test.wsdl")]
  wdr.addAbsolutePathDependencies(file)

  then:
  result == wdr.absolutePathDependencies

  where:
  file                    | result
  new File("schema.xsd")  | [new File("test.wsdl"), new File("schema.xsd")]
  new File("nothing.xsd") | [new File("test.wsdl"), new File("nothing.xsd")]
  new File("test.wsdl")   | [new File("test.wsdl")]

  }
 
  def "we have a list of absolute paths and parse Files, the slurper gets a file that is not in the parseFiles list, but is in the absolute paths list, it shouldn't add to the parse Files list, otherwise you get a circular dependency." () { 
  when: "set up everything"
    wdr.absolutePathDependencies = absolutePaths
    def absoluteFile = FileHelper.getAbsoluteSchemaLocation(location, parentDirectory)
    wdr.addSchemaLocationToParse(absoluteFile)

  then: "parse list should not change"
    wdr.schemaLocationsToParse == []

  where: ""
    absolutePaths             | location        | parentDirectory
    [new File("build/resources/test/Include/OrderNumber.xsd").absoluteFile] | "OrderNumber.xsd" | new File("build/resources/test/Include").absoluteFile

  }

  def "process a list of relative locations from a slurper class" () { 
  given:
  def slurper = new WsdlSlurper()
  slurper.currentDir = file
  
  and: "Partially mock the gatherAllRelativeLocations"
  slurper.metaClass.gatherAllRelativeLocations = { locations }

  when:
  wdr.processRelativeLocations(slurper)

  then:
  wdr.schemaLocationsToParse == locations.collect{ new File(new File(file, it).canonicalPath) }

  where:
  file | locations
  new File("./build/test/resources/wsdl").absoluteFile | ["abstract.wsdl", "../xsd/schema.xsd", "./importWsdl.wsdl"]
  }
}