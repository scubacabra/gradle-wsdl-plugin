package com.jacobo.gradle.plugins.model

import com.jacobo.gradle.plugins.util.ListUtil

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

/**
 *  Slurper for WSDL document processing.  May need to slurp XSD statemenets
 *  embedded in WSDL document, extends #XsdSlurper for this reason
 */
class WsdlSlurper extends XsdSlurper {
    private static final Logger log = Logging.getLogger(WsdlSlurper.class)

    def wsdlImports = []

    /**
     * gathers data from xsd import statments, and from wsdl import statements as well.
     * @param wsdl the #XmlSlurper to gather data from
     */
    def grabWsdlDependencies(wsdl) {
        log.debug("starting to grab WSDL dependencies for {}", wsdl)
        grabWsdlImportedDependencies(wsdl)
        grabWsdlXsdDependencies(wsdl)
        log.debug("grabbed all WSDL dependencies for {}", wsdl)
    }

    /**
     * gathers schema Locations from wsdl import statements (wsdl importing another wsdl)
     * @param wsdl the #XmlSlurper to gather data from
     */
    def grabWsdlImportedDependencies(wsdl) {
        log.debug("resolving this wsdl's 'imported' wsdl dependencies")
        processWsdlDependencyLocations(wsdl?.import)
        log.debug("resolved all wsdl 'imported wsdl dependencies")
    }

    /**
     * grabs wsdl XSD dependencies (embedded in WSDL)
     * Will be in
     * <pre>
     *      <wsdl>
     *          <types>
     *              <schema>
     *                  <xsd:include/>
     *                  <xsd:import/>
     *              </schema>
     *          </types>
     *      </wsdl>
     * </pre>
     * @param wsdl the #XmlSlurper class to gather data from
     */
    def grabWsdlXsdDependencies(wsdl) {
        log.debug("resolving this wsdls XSD Dependencies")
        log.debug("resolving 'includes' XSD dependencies")
        processXsdDependencyLocations(wsdl?.types?.schema?.include)
        log.debug("resolving 'import'(ed) XSD dependencies")
        processXsdDependencyLocations(wsdl?.types?.schema?.import)
    }

    /**
     * process wsdl import statements and populate the #wsdlImports list with the content of the dependent locations
     * @param wsdlSlurperElement is the import Object taken from the #XmlSlurper
     * @see #wsdlImports List
     */
    def processWsdlDependencyLocations = { wsdlSlurperElement ->
        wsdlSlurperElement?.each { wsdlElement ->
            log.debug("the XML slurper element is {}", wsdlElement.name())
            def wsdlImportLocation = wsdlElement.@location.text()
            log.debug("the location is {}", wsdlImportLocation)
            ListUtil.addElementToList(wsdlImports, wsdlImportLocation)
        }
    }

    /**
     * Gathers all relative locations belonging to this instance and packages up into one list
     * @return List of all locations from fields #xsdImports, #xsdIncludes and #wsdlImports
     */
    def gatherAllRelativeLocations() {
        def returnList = super.gatherAllRelativeLocations()
        returnList.addAll(wsdlImports)
        return returnList
    }
}