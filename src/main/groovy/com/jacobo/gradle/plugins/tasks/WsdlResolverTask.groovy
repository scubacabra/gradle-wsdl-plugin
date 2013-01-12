package com.jacobo.gradle.plugins.tasks

import com.jacobo.gradle.plugins.WsdlExtension

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

import org.gradle.api.tasks.TaskAction
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

  final WsdlExtension extension = project.extensions.wsdl

  @TaskAction
  void start() { 
    log.info("finding the wsdl dependencies")
    def dependencyList = wdr.resolveWSDLDependencies()
    log.info("resolving relative paths")
    def resolvedParts = dependencyList.collect { wrpr.resolveRelativePathsToWar(project.rootDir, it)}
    log.info("setting the extension point for war task to use")
    extension.resolved = resolvedParts
  }
} 