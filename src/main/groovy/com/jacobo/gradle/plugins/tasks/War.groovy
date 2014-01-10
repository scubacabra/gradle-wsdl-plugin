package com.jacobo.gradle.plugins.tasks

import org.gradle.api.file.CopySpec
import org.gradle.api.tasks.bundling.War
import org.gradle.api.internal.file.copy.CopySpecImpl

import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

/**
 * Assembles a WAR archive for the wsdl plugin, adds extra functionality to war
 * Populates the WAR with the wsdl folder defined in the build directory
 * Populates the WAR with the schema folder defined in the build directory
 *
 * @author djmijares
 */
class War extends War {

    static final Logger log = Logging.getLogger(War.class)

    /**
     * The name of the folder in the war that all wsdl Files will go into
     */
    @Input
    String wsdlFolder

    /**
     * The name of the folder in the war that all schema (xsd) files will go into
     */
    @Input
    String schemaFolder

    /**
     * The actual directory in the build/$webServicesOutputDir/$wsdlDirectory
     * where all the wsdl files are that this project depends on
     */
    @InputDirectory
    File wsdlDirectory

    /**
     * The actual directory in the build/$webServicesOutputDir/xsdDirectory
     * where all the xsd files are that this project depends on
     */
    @InputDirectory
    File schemaDirectory

    /**
     * I had to do this in the constructor, it was impossible for me, at least
     * when I attemped this, to call super, and then have another method
     * do the into and from.  That was how I did all of the other tasks, but
     * alas, not this one.
     */
    War() {
        super()
        log.debug("Calling war constructor")

        webInf.into({ getWsdlFolder() }) {
            from {
                getWsdlDirectory()
            }
        }

        webInf.into({ getSchemaFolder() }) {
            from {
                getSchemaDirectory()
            }
        }
    }


}