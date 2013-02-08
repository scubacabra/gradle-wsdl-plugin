package com.jacobo.gradle.plugins.tasks

import com.jacobo.gradle.plugins.WsdlExtension

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
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

  final WsdlDependencyResolver wdr = new WsdlDependencyResolver()
  final WsdlWarRelativePathResolver wrpr = new WsdlWarRelativePathResolver()

  @InputFile
  File wsdl
  
  @Input
  File rootDir

  @OutputDirectory
  File resolvedWsdlDir

  @OutputDirectory
  File resolvedSchemaDir

  @OutputDirectory
  File resolvedWebServicesDir

  @TaskAction
  void resolveWsdlDocumentDependencies() { 
    log.info("finding the wsdl document dependencies")
    log.debug("wsdl path: {}", getWsdl())

    wdr.wsdlFile = getWsdl()
    def dependencyList = wdr.resolveWSDLDependencies()
    log.debug("dependency list for this wsdl is {}", dependencyList)

    log.info("resolving all relative paths")

    def relativePathWarResolution = wrpr.resolveRelativePathsToWar(getRootDir(), dependencyList)

    log.debug("relativePathWarResolution list has a size of {} and contains {}", relativePathWarResolution.size(), relativePathWarResolution)

    log.debug("setting the extension point for war task to use with {}", relativePathWarResolution)
    project.wsdl.resolved = relativePathWarResolution

    log.info("copying all web service dependent documents into {}", resolvedWebServicesDir)
    relativePathWarResolution.each { resolved ->
      log.debug("resolving from {} into {} and including these file(s) {}", resolved.from, resolved.into, resolved.resolvedFiles.name)
      ant.copy(toDir: resolvedWebServicesDir.path + File.separator + resolved.into) {
	fileset(dir: resolved.from) {
	  resolved.resolvedFiles.each { file ->
	    include(name: file.name)
	  }
	}
      }
    }
  }

} 