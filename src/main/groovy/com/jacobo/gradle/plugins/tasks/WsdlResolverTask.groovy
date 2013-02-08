package com.jacobo.gradle.plugins.tasks

import com.jacobo.gradle.plugins.WsdlExtension

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Input
import org.gradle.api.DefaultTask

import org.gradle.api.GradleException

import com.jacobo.gradle.plugins.model.WsdlWarRelativePathResolver
import com.jacobo.gradle.plugins.model.WsdlDependencyResolver

/**
 * @author djmijares
 * Created: Mon Jan 07 18:08:42 EST 2013
 */
class WsdlResolverTask extends DefaultTask { 
  static final Logger log = Logging.getLogger(WsdlResolverTask.class)

  static final WsdlDependencyResolver wdr = new WsdlDependencyResolver()
  static final WsdlWarRelativePathResolver wrpr = new WsdlWarRelativePathResolver()

  @InputFile
  File wsdl
  
  @Input
  File rootDirectory

  @TaskAction
  void resolveWsdlDocumentDependencies() { 
    log.info("finding the wsdl document dependencies")
    log.debug("wsdl path: {}", getWsdl())

    wdr.wsdlFile = getWsdl()
    def dependencyList = wdr.resolveWSDLDependencies()

    log.info("resolving all relative paths")

    def relativePathWarResolution = dependencyList.collect { wrpr.resolveRelativePathsToWar(getRootDirectory(), it)}

    log.info("setting the extension point for war task to use with {}", relativePathWarResolution)
    project.wsdl.resolved = relativePathWarResolution

  }
} 