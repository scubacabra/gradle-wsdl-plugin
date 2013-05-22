package com.jacobo.gradle.plugins.tasks

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

import org.gradle.api.tasks.TaskAction

import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.Input
import org.gradle.api.DefaultTask

import org.gradle.api.GradleException

import com.jacobo.gradle.plugins.model.GroupedWsdlWarFiles

/**
 * Uses resolved WSDL dependencies to figure out relative paths between the dependent files and their path relative to the Gradle root directory.
 * Keeps track of duplicate relative paths and stores their files in them, then copies these files to the build directory in a structure
 * <pre>
 *      build/
 *          web-services/
 *              wsdl/
 *              schema/
 * </pre>
 * @author djmijares
 * Created: Mon Jan 07 18:08:42 EST 2013
 */
class WsdlResolverTask extends DefaultTask {
    static final Logger log = Logging.getLogger(WsdlResolverTask.class)

    final GroupedWsdlWarFiles wrpr = new GroupedWsdlWarFiles()

    @Input
    File rootDir

    @Input
    List wsdlDependencies

    @OutputDirectory
    File resolvedWsdlDir

    @OutputDirectory
    File resolvedSchemaDir

    @OutputDirectory
    File resolvedWebServicesDir

    @TaskAction
    void resolveRelativeWarFiles() {

        log.info("resolving all relative paths")

        def relativePathWarResolution = wrpr.resolveRelativePathsToWar(getRootDir(), dependencyList)

        log.debug("relativePathWarResolution list has a size of {} and contains {}", relativePathWarResolution.size(), relativePathWarResolution)

        log.debug("setting the extension point for war task to use with {}", relativePathWarResolution)
        project.wsdl.resolved = relativePathWarResolution

        log.debug("copying all web service dependent documents into {}", getResolvedWebServicesDir())
        relativePathWarResolution.each { resolved ->
            log.debug("resolving from {} into {} and including these file(s) {}", resolved.from, resolved.into, resolved.resolvedFiles.name)
            log.debug("copying into {}", getResolvedWebServicesDir().path + File.separator + resolved.into)
            ant.copy(toDir: getResolvedWebServicesDir().path + File.separator + resolved.into) {
                fileset(dir: resolved.from) {
                    resolved.resolvedFiles.each { file ->
                        include(name: file.name)
                    }
                }
            }
        }
    }

    void resolveWsdlWarFilesToRelativePaths() { 
      
    }

  void copyWsdlWarFilesToOutputDirectory() {

  }

} 