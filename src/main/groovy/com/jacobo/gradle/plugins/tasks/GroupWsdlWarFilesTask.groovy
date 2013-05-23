package com.jacobo.gradle.plugins.tasks

import com.jacobo.gradle.plugins.util.WsdlWarFileGrouper

import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

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
class GroupWsdlWarFilesTask extends DefaultTask {
  static final Logger log = Logging.getLogger(GroupWsdlWarFilesTask.class)

  @Input
  List wsdlDependencies

  @TaskAction
  void groupWsdlWarFiles() {
    log.info("grouping all war files by direct common parent directory")
    def groupedWarFiles = WsdlWarFileGrouper.groupFilesWithCommonParentDirs(getWsdlDependencies())
    log.debug("list of grouped files has a size of {} and contains {}", groupedWarFiles.size(), groupedWarFiles)
    project.wsdl.warFiles = groupedWarFiles
  }
} 