package com.jacobo.gradle.plugins.model

import com.jacobo.gradle.plugins.BaseSpecification
import com.jacobo.gradle.plugins.util.FileHelper
import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

/**
 * @author jacobono
 * Created: Mon Jan 07 18:08:42 EST 2013
 */
class WsdlDependencyResolverSpec extends BaseSpecification { 

  def WsdlDependencyResolver wdr = new WsdlDependencyResolver()

  def "Resolve dependency file, if not resolved and stored in projectDependencies, need to add to unresolved Dependencies and parse -- unless it is already in the unresolvedDependencies data field"() {   
  
  when:
  wdr.unresolvedDependencies = [new File("test.wsdl")] as Set
  wdr.projectDependencies = [new File("resolved.wsdl")] as Set
  wdr.resolveDependency(file)

  then:
  result as Set == wdr.unresolvedDependencies

  where:
  file                    | result
  new File("schema.xsd")  | [new File("test.wsdl"), new File("schema.xsd")] // new unresolved
  new File("nothing.xsd") | [new File("test.wsdl"), new File("nothing.xsd")] // new unresolved
  new File("test.wsdl")   | [new File("test.wsdl")] // same unresolved dependency, shouldn't add again
  new File("resolved.wsdl")   | [new File("test.wsdl")] // don't add it's already resolved

  }
 
  def "project Dependencies are populated and the absolute File to an existing project Dependency is encountered, unresolved Dependencies should not add this file" () { 
  when: "set up everything"
    wdr.projectDependencies = absolutePaths as Set
    def absoluteFile = FileHelper.getAbsoluteSchemaLocation(location, parentDirectory) // would  come form slurpedDocuments documentDependencies
    wdr.resolveDependency(absoluteFile)

  then: "unresolved dependencies should be empty"
    wdr.unresolvedDependencies.isEmpty() == true

  where: ""
    absolutePaths             | location        | parentDirectory
    [new File("build/resources/test/Include/OrderNumber.xsd").absoluteFile] | "OrderNumber.xsd" | new File("build/resources/test/Include").absoluteFile

  }

  def "add all Absolute File objects in documentDependencies, these haven't been processed yet" () { 
  given: "set up Document Slurper"
    def slurper = new WsdlSlurper()
    slurper.documentFile = new File("some_file")
    slurper.documentDependencies = locations.collect{ getFileFromResourcePath(it) } as Set 

  when: "resolving Dependencies from slurped Document"
    wdr.resolveDocumentDependencies(slurper)

  then: "unresolved Dependencies should be the absolute file locations"
    wdr.unresolvedDependencies == locations.collect{ getFileFromResourcePath(it).absoluteFile } as Set

  where: "resource locations for dependencies are"
    locations =  ["/wsdl/abstract.wsdl", "/schema/Include/Product.xsd", "/schema/Include/OrderNumber.xsd"]
  }
}