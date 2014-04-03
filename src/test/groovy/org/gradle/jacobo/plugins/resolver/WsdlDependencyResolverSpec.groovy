package org.gradle.jacobo.plugins.resolver

import spock.lang.Specification
import spock.lang.Unroll

import org.gradle.jacobo.schema.WsdlDocument
import org.gradle.jacobo.schema.XsdDocument
import org.gradle.jacobo.schema.factory.DocumentFactory
import org.gradle.jacobo.schema.slurper.DocumentSlurper
import org.gradle.jacobo.schema.slurper.XsdSlurper
import org.gradle.jacobo.schema.resolver.DocumentResolver

class WsdlDependencyResolverSpec extends Specification {
  def docFactory = Mock(DocumentFactory)
  def docSlurper = Mock(DocumentSlurper)
  def docResolver = Mock(DocumentResolver)
  def xsdSlurper = Mock(XsdSlurper)
  def resolver = new WsdlDependencyResolver(docFactory)

  // file to build dependencies around
  def files = ["file.wsdl": new File("file.wsdl"), "b.wsdl":new File("b.wsdl"),
	       "a.xsd":new File("a.xsd"), "b.xsd":new File("b.xsd"),
	       "c.xsd":new File("c.xsd"), "d.xsd":new File("d.xsd"),
	       "e.xsd":new File("e.xsd"), "f.xsd":new File("f.xsd"),
	       "g.xsd":new File("g.xsd")];

  // mock/fake the WsdlDocument return by the DocumentFactory interface
  // only important field to set is the file and documentDependencies
  def mockWsdl(File file, Map<String, File> dependencies) {
    def slurped = null // no slurper for this unit test, unnecessary
    def wsdlDocument = new WsdlDocument(docSlurper, docResolver, file, slurped)
    // only important field, gets set during wsdlDocument.slurp() but we are mocking it here
    wsdlDocument.documentDependencies = dependencies
    return wsdlDocument
  }

  // mock/fake the XsdDocument return by the DocumentFactory interface
  // only important field to set is the file and documentDependencies
  def mockXsd(File file, Map<String, File> dependencies) {
    def slurped = null // no slurper for this unit test, unnecessary field
    def xsdDocument = new XsdDocument(docSlurper, docResolver, xsdSlurper, file, slurped)
    // only important field, gets set during xsdDocument.slurp() but we are mocking it here
    xsdDocument.documentDependencies = dependencies
    return xsdDocument
  }

  /**
   * All unique dependencies.  The dependencies look like
   * file.wsdl
   *   - b.wsdl (wsdl import)
   *   - a.xsd (xsd import)
   *     - c.xsd (xsd import)
   *     - d.xsd (xsd import)
   *   - b.xsd (xsd import)
   *     - e.xsd (xsd import)
   *     - f.xsd (xsd import)
   *     - g.xsd (xsd include)
   */
  def "resolve wsdl dependencies, all unique"() {
    given: "set up the dependencies"
    def wsdl = mockWsdl(files["file.wsdl"],
			["b.wsdl":files["b.wsdl"], "a.xsd":files["a.xsd"],
			 "b.xsd":files["b.xsd"]])
    def bwsdl = mockWsdl(files["b.wsdl"], [:])
    def axsd = mockXsd(files["a.xsd"],
		       ["c.xsd":files["c.xsd"], "d.xsd":files["d.xsd"]])
    def bxsd = mockXsd(files["b.xsd"],
		       ["e.xsd":files["e.xsd"], "f.xsd":files["f.xsd"],
		       "g.xsd":files["g.xsd"]])
    def cxsd = mockXsd(files["c.xsd"], [:])
    def dxsd = mockXsd(files["d.xsd"], [:])
    def exsd = mockXsd(files["e.xsd"], [:])
    def fxsd = mockXsd(files["f.xsd"], [:])
    def gxsd = mockXsd(files["g.xsd"], [:])    
  
    when:
    def result = resolver.resolveDependencies(files["file.wsdl"])
    
    then:
    1 * docFactory.createDocument(files["file.wsdl"]) >> wsdl
    1 * docFactory.createDocument(files["b.wsdl"]) >> bwsdl
    1 * docFactory.createDocument(files["a.xsd"]) >> axsd
    1 * docFactory.createDocument(files["b.xsd"]) >> bxsd
    1 * docFactory.createDocument(files["c.xsd"]) >> cxsd
    1 * docFactory.createDocument(files["d.xsd"]) >> dxsd
    1 * docFactory.createDocument(files["e.xsd"]) >> exsd
    1 * docFactory.createDocument(files["f.xsd"]) >> fxsd
    1 * docFactory.createDocument(files["g.xsd"]) >> gxsd
    result.size() == 9
    result.each { it instanceof File }
    files.each { k, v -> 
      result.contains(v)
    }
  }

  /**
   * Some duplicate dependencies.  The dependencies look like
   * file.wsdl
   *   - b.wsdl (wsdl import)
   *   - a.xsd (xsd import)
   *     - c.xsd (xsd import)
   *     - d.xsd (xsd import) #duplicate
   *     - g.xsd (xsd include) #duplicate
   *   - b.xsd (xsd import)
   *     - d.xsd (xsd import) #duplicate
   *     - e.xsd (xsd import)
   *     - f.xsd (xsd import)
   *     - g.xsd (xsd include) #duplicate
   */
  def "resolve wsdl dependencies, skip already resolved imports"() {
    given: "set up the dependencies"
    def wsdl = mockWsdl(files["file.wsdl"],
			["b.wsdl":files["b.wsdl"], "a.xsd":files["a.xsd"],
			 "b.xsd":files["b.xsd"]])
    def bwsdl = mockWsdl(files["b.wsdl"], [:])
    def axsd = mockXsd(files["a.xsd"],
		       ["c.xsd":files["c.xsd"], "d.xsd":files["d.xsd"],
			"g.xsd":files["g.xsd"]])
    def bxsd = mockXsd(files["b.xsd"],
		       ["d.xsd":files["d.xsd"], "e.xsd":files["e.xsd"],
			"f.xsd":files["f.xsd"], "g.xsd":files["g.xsd"]])
    def cxsd = mockXsd(files["c.xsd"], [:])
    def dxsd = mockXsd(files["d.xsd"], [:])
    def exsd = mockXsd(files["e.xsd"], [:])
    def fxsd = mockXsd(files["f.xsd"], [:])
    def gxsd = mockXsd(files["g.xsd"], [:])

    when:
    def result = resolver.resolveDependencies(files["file.wsdl"])
    
    then:
    1 * docFactory.createDocument(files["file.wsdl"]) >> wsdl
    1 * docFactory.createDocument(files["b.wsdl"]) >> bwsdl
    1 * docFactory.createDocument(files["a.xsd"]) >> axsd
    1 * docFactory.createDocument(files["b.xsd"]) >> bxsd
    1 * docFactory.createDocument(files["c.xsd"]) >> cxsd
    1 * docFactory.createDocument(files["d.xsd"]) >> dxsd
    1 * docFactory.createDocument(files["e.xsd"]) >> exsd
    1 * docFactory.createDocument(files["f.xsd"]) >> fxsd
    1 * docFactory.createDocument(files["g.xsd"]) >> gxsd
    result.size() == 9
    result.each { it instanceof File }
    files.each { k, v -> 
      result.contains(v)
    }
  }

  def "wsdl resolver encounter no dependencies, just its own wsdl resolved"() {
    given:
    def file = new File("wsdl-no-deps.wsdl")
    def wsdlDocument = mockWsdl(file, [:]) // no deps

    when:
    def result = resolver.resolveDependencies(file)

    then:
    1 * docFactory.createDocument(_) >> wsdlDocument // mock WSDL Document no deps
    result.size() == 1
  }
}