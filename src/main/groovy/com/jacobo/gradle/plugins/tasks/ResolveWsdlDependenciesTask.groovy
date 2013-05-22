package com.jacobo.gradle.plugins.tasks

import com.jacobo.gradle.plugins.model.WsdlDependencyResolver

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

/**
 * Resolve WSDL dependencies into absolute File Paths
 * User: djmijares
 * Date: 5/22/13
 * Time: 10:56 AM
 */
class ResolveWsdlDependenciesTask extends DefaultTask {
    static final Logger log = Logging.getLogger(WsdlResolverTask.class)

    final WsdlDependencyResolver wdr = new WsdlDependencyResolver()

    @InputFile
    File wsdl

    @TaskAction
    void resolveWsdlDependencies() {
       log.info("finding the wsdl document dependencies")
       log.debug("wsdl path: {}", getWsdl())
       wdr.wsdlFile = getWsdl()
       def dependencyList = wdr.resolveWSDLDependencies()
       log.debug("dependency list for this wsdl is {}", dependencyList)
       project.wsdl.wsdlDependencies = dependencyList
    }

}
