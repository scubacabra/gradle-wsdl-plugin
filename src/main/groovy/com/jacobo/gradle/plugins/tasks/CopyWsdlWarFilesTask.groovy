package com.jacobo.gradle.plugins.tasks

import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.Input
import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException

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
class CopyWsdlWarFilesTask extends DefaultTask {
    static final Logger log = Logging.getLogger(CopyWsdlWarFilesTask.class)

    @Input
    File rootDir

    @Input
    List warFiles

    @OutputDirectory
    File resolvedWsdlDir

    @OutputDirectory
    File resolvedSchemaDir

    @OutputDirectory
    File resolvedWebServicesDir

    @TaskAction
    void resolveRelativeWarFiles() {
        log.debug("copying all web service dependent documents into {}", getResolvedWebServicesDir())
        getWarFiles().each { warFile ->
            log.debug("copying from {} and including these file(s) {}", warFile.groupedFolder, warFile.groupedFiles)

            log.debug("copying into {}", getResolvedWebServicesDir().path + File.separator + warFile.into)
            ant.copy(toDir: getResolvedWebServicesDir().path + File.separator + warFile.into) { //copy to
                fileset(dir: warFile.groupedFolder.canonicalPath) { // from
                    warFile.groupedFiles.each { fileName -> //include files
                        include(name: fileName)
                    }
                }
            }
        }
    }
} 