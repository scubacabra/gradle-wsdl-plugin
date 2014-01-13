package com.jacobo.gradle.plugins.tasks

import com.jacobo.gradle.plugins.model.GroupedWarFiles

import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.Input
import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException

import com.google.common.annotations.VisibleForTesting
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
class CopyWarFiles extends DefaultTask {
  static final Logger log = Logging.getLogger(CopyWarFiles.class)

  /**
   * Project Root Dir, NOT Project Directory, unless they are one in the same
   */
  @Input
  File projectRootDir

  /**
   * Files grouped by GroupWarFiles task
   */
  @Input
  List<GroupedWarFiles> warFiles

  /**
   * Output directory for all files to copied into
   * This folder will be in the 'build' folder for the project
   */
  @OutputDirectory
  File webServicesCopyDir

  /**
   * Iterate over all grouped war files, find the correct relative Path to copy into the the webServicesCopyDir
   */
  @TaskAction
  @VisibleForTesting
  void copyWarFilesToOutputDir() {
    log.info("Copying all web service dependent documents into '{}'", getWebServicesCopyDir())
    getWarFiles().each { warFile ->
      log.debug("Copying '{}' from '{}'", warFile.groupedFiles, warFile.groupedFolder)
      def outDir = getWebServicesCopyDir().path + File.separator + findRelativePath(warFile.groupedFolder, getProjectRootDir())
      log.debug("copying into {}", outDir)

      ant.copy(toDir: outDir) { //copy to
	fileset(dir: warFile.groupedFolder.canonicalPath) { // from
	  warFile.groupedFiles.each { fileName -> //include files
	    include(name: fileName)
	  }
	}
      }

    }
  }

  /**
   * Finds the Relative file path
   * Input - from (the longer Path)
   * Input - relativeTo (the shorter Path to find the relative path against)
   * Output - string of the relative path
   * File separator is necessary, because Java files don't put these at the end of paths for directories, and this is dealing with directories
   */
  def findRelativePath(File from, File relativeTo) {
    def relativePath = from.canonicalPath.replaceFirst(relativeTo.canonicalPath + File.separator, "")
    log.debug("'{}' is the relative path of '{}' relative to '{}'", relativePath, from.canonicalPath, relativeTo.canonicalPath + File.separator)
    return relativePath
  }
} 