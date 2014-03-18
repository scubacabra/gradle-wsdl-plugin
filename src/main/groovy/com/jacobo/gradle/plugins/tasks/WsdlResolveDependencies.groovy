package com.jacobo.gradle.plugins.tasks

import org.gradle.api.tasks.bundling.War

import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.InputFile

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

import com.jacobo.gradle.plugins.resolve.DependencyResolver

/**
 * Resolves WSDL dependencies starting with the WSDL file
 *
 * @author djmijares
 */
class WsdlResolveDependencies extends War {
  static final Logger log = Logging.getLogger(War.class)

  /**
   * wsdl file (absolute path)
   */
  @InputFile
  File wsdlFile

  /**
   * Resolves all wsdl Dependencies through XmlSlurping
   */
  DependencyResolver dependencyResolver

  @TaskAction
  void start() {
    def wsdlDependencies = dependencyResolver.resolveDependencies(getWsdlFile())
    project.wsdl.wsdlDependencies = project.files(wsdlDependencies)
  }
}