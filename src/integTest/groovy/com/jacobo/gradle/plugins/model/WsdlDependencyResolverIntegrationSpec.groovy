package com.jacobo.gradle.plugins.model

import com.jacobo.gradle.plugins.BaseSpecification

class WsdlDependencyResolverIntegrationSpec extends BaseSpecification {
  
  def wdr = new WsdlDependencyResolver()
  
  def "test the full resolver with WSDLs and XSDs" () { 
    
  when:
  def wsdlFile = getFileFromResourcePath(path)
  def result = wdr.resolveProjectDependencies(wsdlFile)

  then:
  expected.collect{ getFileFromResourcePath(it) }*.absoluteFile.each{ result.contains(it) }
  expected.size == result.size()

  where:
  path						| expected
  "/wsdl/noXsdImport.wsdl"			| ["/wsdl/noXsdImport.wsdl"]
  "/wsdl/OneXsdImport.wsdl"			| ["/wsdl/OneXsdImport.wsdl", "/schema/Messages/Messages.xsd"]
  "/wsdl/TwoXsdImports.wsdl"			| ["/wsdl/TwoXsdImports.wsdl", "/schema/PO/PurchaseOrder.xsd", "/schema/Messages/Messages.xsd"]
  "/wsdl/XsdImportXsdAndIncludesXsd.wsdl"	| ["/wsdl/XsdImportXsdAndIncludesXsd.wsdl", "/schema/Include/OrderNumber.xsd", "/schema/Include/include2.xsd", "/schema/Include/include.xsd", "/schema/Include/Product.xsd", "/schema/Messages/Messages.xsd"]
  "/wsdl/ImportsAnotherWsdl.wsdl"		| ["/wsdl/ImportsAnotherWsdl.wsdl", "/wsdl/abstract.wsdl", "/schema/Messages/Messages.xsd"] 
  }
}