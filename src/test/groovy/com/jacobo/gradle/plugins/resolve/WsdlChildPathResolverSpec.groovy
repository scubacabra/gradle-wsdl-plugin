package com.jacobo.gradle.plugins.resolve

import spock.lang.Specification
import spock.lang.Unroll

class WsdlChildPathResolverSpec extends Specification {
  
  def resolver = new WsdlChildRelativePathResolver()

  // testing inputs
  def wsdlDir = new File("/wsdl").absoluteFile
  def schemaDir = new File("/schema").absoluteFile
  def wsdlFiles = ["a.wsdl", "b.wsdl", "abstract/abstract.wsdl"]
  def schemaFiles = ["pkg1/a.xsd", "pkg1/b.xsd", "pkg1/c.xsd", "pkg2/d.xsd", "pkg2/e.xsd", "pkg2/f.xsd"]
  def resolvedFiles = [] as Set

  def setup() {
    wsdlFiles.each {
      resolvedFiles.add(new File(wsdlDir, it).absoluteFile)
    }
    schemaFiles.each {
      resolvedFiles.add(new File(schemaDir, it).absoluteFile)
    }
  }

  def "find relative paths under the wsdl dir"() {
    when:
    def results = resolver.resolvePaths(wsdlDir, resolvedFiles)

    then:
    results.size() == 3
    results == wsdlFiles
  }

  def "find relative paths under the xsd/schema dir"() {
    when:
    def results = resolver.resolvePaths(schemaDir, resolvedFiles)

    then:
    results.size() == 6
    results == schemaFiles
  }
}