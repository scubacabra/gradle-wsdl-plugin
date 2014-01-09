package com.jacobo.gradle.plugins.model

import com.jacobo.gradle.plugins.BaseSpecification

class WsdlDependencyResolverIntegrationSpec extends BaseSpecification {
  
  def wdr = new WsdlDependencyResolver()
  
  def "test the full resolver with WSDLs and XSDs" () { 
    
  when:
  def wsdlFile = getFileFromResourcePath(path)
  def result = wdr.resolveProjectDependencies(wsdlFile)

  then:
  expected*.absoluteFile.each{ result.contains(it) }
  expected.size == result.size()

  where:
  path | expected
  "/wsdl/noXsdImport.wsdl" | [new File("build/resources/test/wsdl/noXsdImport.wsdl")]
  "/wsdl/OneXsdImport.wsdl" | [new File("build/resources/test/wsdl/OneXsdImport.wsdl"), new File("build/resources/test/schema/Messages/Messages.xsd")]
  "/wsdl/TwoXsdImports.wsdl" | [new File("build/resources/test/wsdl/TwoXsdImports.wsdl"), new File("build/resources/test/schema/PO/PurchaseOrder.xsd"), new File("build/resources/test/schema/Messages/Messages.xsd")]
  "/wsdl/XsdImportXsdAndIncludesXsd.wsdl" | [new File("build/resources/test/wsdl/XsdImportXsdAndIncludesXsd.wsdl"), new File("build/resources/test/schema/Include/OrderNumber.xsd"), new File("build/resources/test/schema/Include/include2.xsd"), new File("build/resources/test/schema/Include/include.xsd"), new File("build/resources/test/schema/Include/Product.xsd"), new File("build/resources/test/schema/Messages/Messages.xsd")]
  "/wsdl/ImportsAnotherWsdl.wsdl" | [new File("build/resources/test/wsdl/ImportsAnotherWsdl.wsdl"), new File("build/resources/test/wsdl/abstract.wsdl"), new File("build/resources/test/schema/Messages/Messages.xsd")] 
  }
}