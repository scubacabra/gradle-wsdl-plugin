package com.jacobo.gradle.plugins.tasks

import com.jacobo.gradle.plugins.model.WsdlDependencyResolver

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

/**
 * Resolve WSDL Dependencies for a WSDL file (associated with a project)
 * User: jacobono
 * Date: 5/22/13
 * Time: 10:56 AM
 */
class ResolveWsdlDependencies extends DefaultTask {
    static final Logger log = Logging.getLogger(ResolveWsdlDependencies.class)

    final WsdlDependencyResolver wdr = new WsdlDependencyResolver()

    /**
     * File object to the main WSDL this project uses
     */
    @InputFile
    File wsdlDocument

    /**
     * delegates to process project WSDL and XSD dependencies
     * populates wsdlDependency List from the return Set of projectDependencies
     */
    @TaskAction
    void resolveWsdlDependencies() {
      log.info("Finding the WSDL dependencies for '{}'", getWsdlDocument().name)
      def projectDependencies = wdr.resolveProjectDependencies(getWsdlDocument())
      project.wsdl.wsdlDependencies = new ArrayList(projectDependencies)
    }

}
