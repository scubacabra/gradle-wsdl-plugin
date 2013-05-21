package com.jacobo.gradle.plugins.reader

import com.jacobo.gradle.plugins.model.WsdlSlurper
import com.jacobo.gradle.plugins.model.XsdSlurper

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

/**
 * Slurps a file object with @see XmlSlurper
 * @author djmijares
 */
class DocumentReader {
  private static final Logger log = Logging.getLogger(DocumentReader.class)  

  /**
   * @param document is a File to be slurped by @see XmlSlurper
   * Slurp document
   * @return @see WsdlSlurper
   * @return @see XsdSlurper
   */
  static slurpDocument(File document) { 
    log.debug("file is {}", document)
    log.debug("current Dir is {}", document.parentFile)
    def slurped = new XmlSlurper().parse(document)
    def slurper
    if(document.name.split("\\.")[-1] == 'xsd') { // either xsd or wsdl
      slurper = new XsdSlurper()
      slurper.grabXsdDependencies(slurped)	
    } else {
      slurper = new WsdlSlurper()
      slurper.grabWsdlDependencies(slurped)
    }
    slurper.currentDir = document.parentFile
    slurper.documentName = document.name
    return slurper
  }
}