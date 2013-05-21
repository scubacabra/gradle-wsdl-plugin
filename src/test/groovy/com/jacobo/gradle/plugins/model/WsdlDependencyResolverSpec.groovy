package com.jacobo.gradle.plugins.model

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
 
  def "test absolute schema location"() { 
  expect:
  result == wdr.getAbsoluteSchemaLocation(schemaLocale, parent)

  where:
  parent             | schemaLocale        | result
  new File("schema") | "../blah/blah/blah" | new File("blah/blah/blah").absoluteFile
  new File("wsdl")   | "../blah/something" | new File("blah/something").absoluteFile
  new File("nothing")| "../blah/blah/blah" | new File("blah/blah/blah").absoluteFile
  }


  def "we have a list of absolute paths and parse Files, the slurper gets a file that is not in the parseFiles list, but is in the absolute paths list, it shouldn't add to the parse Files list, otherwise you get a circular dependency." () { 
  when: "set up everything"
    wdr.absolutePathDependencies = absolutePaths
    def absoluteFile = wdr.getAbsoluteSchemaLocation(location, parentDirectory)
    wdr.addSchemaLocationToParse(absoluteFile)

  then: "parse list should not change"
    wdr.schemaLocationsToParse == []

  where: ""
    absolutePaths             | location        | parentDirectory
    [new File("build/resources/test/Include/OrderNumber.xsd").absoluteFile] | "OrderNumber.xsd" | new File("build/resources/test/Include").absoluteFile

  }

  def "process a list of relative locations from a slurper class" () { 
  setup:
  def slurper = [currentDir: file]
  
  when:
  wdr.processRelativeLocations(locations, slurper)

  then:
  wdr.schemaLocationsToParse == locations.collect{ new File(new File(file, it).canonicalPath) }

  where:
  file | locations
  new File("./build/test/resources/wsdl").absoluteFile | ["abstract.wsdl", "../xsd/schema.xsd", "./importWsdl.wsdl"]
  }
}