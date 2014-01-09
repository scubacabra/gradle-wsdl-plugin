package com.jacobo.gradle.plugins.model

import com.jacobo.gradle.plugins.BaseSpecification

class WsdlSlurperSpec extends BaseSpecification {
  
  def slurper = new WsdlSlurper()
  
  def "wsdl dependency locations" () {
  setup:
  def file = getFileFromResourcePath(filePath)
  slurper.slurpedDocument = new XmlSlurper().parse(file)
  slurper.documentFile = file

  when:
  slurper.resolveDocumentDependencies()

  then:
  slurper.wsdlImports == wsdlImports as Set
  slurper.xsdImports == xsdImports as Set
  slurper.xsdIncludes == xsdIncludes as Set
  slurper.documentDependencies == dependencyLocations.collect { getFileFromResourcePath(it) } as Set

  where:
  filePath				| wsdlImports		| xsdImports	| xsdIncludes	|dependencyLocations
  "/wsdl/noXsdImport.wsdl"		| []			| []		| []		| []
  "/wsdl/ImportsAnotherWsdl.wsdl"	| ["./abstract.wsdl"]	| ["../schema/Messages/Messages.xsd"]		| []		| ["/schema/Messages/Messages.xsd", "/wsdl/abstract.wsdl"]
  }
}