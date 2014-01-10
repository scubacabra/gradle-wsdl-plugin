package com.jacobo.gradle.plugins.tasks

import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

import com.jacobo.gradle.plugins.model.GroupedWarFiles
import com.google.common.annotations.VisibleForTesting

/**
 * Uses commonDirectory WSDL dependencies to figure out relative paths between the dependent files and their path relative to the Gradle root directory.
 * Keeps track of duplicate relative paths and stores their files in them, then copies these files to the build directory in a structure
 * <pre>
 *      build/
 *          web-services/
 *              wsdl/
 *              schema/
 * </pre>
 * @author jacobono
 * Created: Mon Jan 07 18:08:42 EST 2013
 */
class GroupWarFiles extends DefaultTask {
  static final Logger log = Logging.getLogger(GroupWarFiles.class)

  @Input
  List wsdlDependencies

  @TaskAction
  void groupWsdlWarFiles() {
    log.info("Grouping all WAR files by their common, shared, directories")
    project.wsdl.warFiles = this.groupWarFilesByCommonDirectories(getWsdlDependencies())
  }

  /**
   * Takes a root directory and a list of absolute file dependencies and group file names by their common, shared directories.
   * Compares them against the root Dir of the project, finds their relative path files, groups like relative paths together
   * This handles the 'into' and 'from' and the 'included' files to go into copied WAR diretory in the build folder*
   * @param ungroupedFiles a list of ungrouped absolute File paths, that are to be included in the WAR artifact, that may/may not share parent directories
   * @return List<GroupedWsdlWarFiles>
   */
  @VisibleForTesting
  List<GroupedWarFiles> groupWarFilesByCommonDirectories(List<File> ungroupedFiles) {
    def groupedDirectories = []
    ungroupedFiles.each { ungroupedFile ->
      def commonDirectory = groupedDirectories.find { it.groupedFolder == ungroupedFile.parentFile }
      
      if(commonDirectory) {
	log.debug("File '{}' has a parent directory '{}', that is already grouped", ungroupedFile.name, commonDirectory.groupedFolder)
	commonDirectory.groupedFiles << ungroupedFile.name
	return true
      }
      
      log.debug("File '{}' has a parent directory '{}', that needs to be grouped", ungroupedFile.name, ungroupedFile.parentFile)
      def grouping = new GroupedWarFiles(groupedFolder: ungroupedFile.parentFile)
      grouping.groupedFiles << ungroupedFile.name
      groupedDirectories << grouping
      
    }

    log.debug("Grouped Files by '{}' different directories", groupedDirectories.size())
    return groupedDirectories
  }
} 