package com.jacobo.gradle.plugins.util

import spock.lang.Specification

class ListHelperSpec extends Specification {
  
  def "is Already in List works out well" () { 

  when:
  def list = [new File("schema.xsd"), new File("test.wsdl")]

  then:
  result == ListHelper.isAlreadyInList(list, file)

  where:
  file                | result
  new File("schema.xsd")  | true
  new File("test.wsdl")   | true
  new File("nothing.xsd") | false

  }

  def "add element to List correctly" () {
  setup:
  def list = [new File("schema.xsd"), new File("test.wsdl")]

  when:
  ListHelper.addElementToList(list, file)

  then:
  result == list

  where:
  file                | result
  new File("schema.xsd")  | [new File("schema.xsd"), new File("test.wsdl")]
  new File("test.wsdl")   | [new File("schema.xsd"), new File("test.wsdl")]
  new File("nothing.xsd") | [new File("schema.xsd"), new File("test.wsdl"), new File("nothing.xsd")]

  }
}