package com.jacobo.gradle.plugins.tasks

import org.gradle.api.file.CopySpec
import org.gradle.api.tasks.bundling.War
import org.gradle.api.internal.file.copy.CopySpecImpl

import org.gradle.api.tasks.TaskAction

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

import com.jacobo.gradle.plugins.model.WsdlDependencyResolver
import com.jacobo.gradle.plugins.model.WsdlWarRelativePathResolver
import com.jacobo.gradle.plugins.model.WsdlName

/**
 * Assembles a WAR archive for the wsdl plugin, adds extra functionality to war
 *
 * @author Daniel Mijares
 */
class WsdlWarTask extends War { 

  static final Logger log = Logging.getLogger(WsdlWarTask.class)

  WsdlName nameConvention = new WsdlName()
  WsdlDependencyResolver dependencyResolver = new WsdlDependencyResolver()
  WsdlWarRelativePathResolver resolvePaths = new WsdlWarRelativePathResolver()

  WsdlWarTask() { 

    super()					     
    log.debug("calling the war task constructor")

    def wsdlName = nameConvention.findWsdlFileName(project.name)
    log.info("wsdl name is {}", wsdlName)

    def wsdlFile = new File(project.extensions.wsdl.wsdlDirectory, wsdlName + ".wsdl")
    log.info("wsdl file is {}", wsdlFile)

    dependencyResolver.wsdlFile = wsdlFile
    def resolveList = dependencyResolver.resolveWSDLDependencies()
    log.info("resolve List is : {}", resolveList)

    def resolved = resolveList.collect { resolvePaths.resolveRelativePathsToWar(project.rootDir, it) }
    resolved.each { resolvee ->
      log.info("from : {}, into : {}, include: {}", resolvee.from, resolvee.into, resolvee.include)
      getWebInf().from (resolvee.from)  {
	into  resolvee.into
	include resolvee.include
      }
    }
  }
  
  @TaskAction
  def void addSchemas() {

  } 
}