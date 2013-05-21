package com.jacobo.gradle.plugins.model

import spock.lang.Specification

class WsdlDependencyResolverIntegrationSpec extends Specification {
  
  def wdr = new WsdlDependencyResolver()
  
  def "test the full resolver with WSDLs and XSDs" () { 
    
  when:
  def wsdlFile = new File(url.toURI())

  then:
  result*.absoluteFile == wdr.resolveWSDLDependencies(wsdlFile)
  //TODO this expectation result could be a little more dynamic, in other words the list should HAVE to be in order, just contain the same elements.  see what you can do later
  where:
  url | result
  this.getClass().getResource("/wsdl/noXsdImport.wsdl") | [new File("build/resources/test/wsdl/noXsdImport.wsdl")]
  this.getClass().getResource("/wsdl/OneXsdImport.wsdl") | [new File("build/resources/test/wsdl/OneXsdImport.wsdl"), new File("build/resources/test/schema/Messages/Messages.xsd")]
  this.getClass().getResource("/wsdl/TwoXsdImports.wsdl") | [new File("build/resources/test/wsdl/TwoXsdImports.wsdl"), new File("build/resources/test/schema/PO/PurchaseOrder.xsd"), new File("build/resources/test/schema/Messages/Messages.xsd")]
  this.getClass().getResource("/wsdl/XsdImportXsdAndIncludesXsd.wsdl") | [new File("build/resources/test/wsdl/XsdImportXsdAndIncludesXsd.wsdl"), new File("build/resources/test/schema/Include/OrderNumber.xsd"), new File("build/resources/test/schema/Include/include2.xsd"), new File("build/resources/test/schema/Include/include.xsd"), new File("build/resources/test/schema/Include/Product.xsd"), new File("build/resources/test/schema/Messages/Messages.xsd")]
  this.getClass().getResource("/wsdl/ImportsAnotherWsdl.wsdl") | [new File("build/resources/test/wsdl/ImportsAnotherWsdl.wsdl"), new File("build/resources/test/wsdl/abstract.wsdl"), new File("build/resources/test/schema/Messages/Messages.xsd")] 
  }
}