package com.jacobo.gradle.plugins.resolve

import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

class WsdlChildRelativePathResolver implements ChildrenPathResolver {
  private static final Logger log = Logging.getLogger(WsdlChildRelativePathResolver.class)

  def filesUnderDirectory = { dir, file -> file.absolutePath.contains(dir.absolutePath) }
  def childrenPaths = { dir, file -> file.absolutePath - "${dir.absolutePath}${File.separator}"}

  /**
   * Resolve paths relative to a parent Dir.
   * Will get relative paths of files relative to a directory
   * @param parentDir the directory to get relative paths relative to
   * @param files Set of files that contain paths
   * @return list of relative paths
   */  
  public List resolvePaths(File parentDir, Set<File> files) {
    log.debug("finding files under '{}'", parentDir)
    def relativePaths = files.findAll(
      filesUnderDirectory.curry(parentDir)).collect(childrenPaths.curry(parentDir))
    return relativePaths
  }
}