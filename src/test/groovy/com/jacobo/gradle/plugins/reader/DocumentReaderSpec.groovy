package com.jacobo.gradle.plugins.reader

import com.jacobo.gradle.plugins.BaseSpecification
import com.jacobo.gradle.plugins.model.WsdlSlurper
import com.jacobo.gradle.plugins.model.XsdSlurper

class DocumentReaderSpec extends BaseSpecification {
  
  def "slurp Document get the right type of slurper to return" () {

  when:
  def file = getFileFromResourcePath(filePath)
  def result = DocumentReader.slurpDocument(file)

  then:
  result.class in [clazz]
  result.slurpedDocument != null
  result.documentFile == file
  result.documentDependencies.isEmpty() == isEmpty // tested in WsdlSlurperSpec and XsdSlurperSpec
  
  where:
  filePath				| clazz		| isEmpty
  "/wsdl/noXsdImport.wsdl"		| WsdlSlurper	| true
  "/schema/PO/PurchaseOrder.xsd"	| XsdSlurper	| true
  }
}