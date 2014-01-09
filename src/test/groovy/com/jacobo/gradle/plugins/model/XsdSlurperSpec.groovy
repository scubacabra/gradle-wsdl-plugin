package com.jacobo.gradle.plugins.model

import com.jacobo.gradle.plugins.BaseSpecification
import spock.lang.Unroll

class XsdSlurperSpec extends BaseSpecification {
  
  def slurper = new XsdSlurper()

  @Unroll("resolved Dependencies for #file is 'imports':'#imports' and 'includes':'#includes'")
  def "resolve XSD Document Dependencies" () {
  setup:
  def file = getFileFromResourcePath(filePath)
  slurper.slurpedDocument = new XmlSlurper().parse(file)
  slurper.documentFile = file

  when:
  slurper.resolveDocumentDependencies()

  then:
  slurper.xsdImports == imports as Set
  slurper.xsdIncludes == includes as Set
  slurper.documentDependencies == dependencyLocations.collect{ getFileFromResourcePath(it) } as Set

  where:
  filePath				| imports		| includes				| dependencyLocations
  "/schema/Include/OrderNumber.xsd"	| ["Product.xsd"]	| ["include.xsd", "include2.xsd"]	| ["/schema/Include/Product.xsd", "/schema/Include/include.xsd", "/schema/Include/include2.xsd"]
  "/schema/Include/Product.xsd"		| []			| []					| []
  "/schema/PO/PurchaseOrder.xsd"	| []			| []					| []

  }
}