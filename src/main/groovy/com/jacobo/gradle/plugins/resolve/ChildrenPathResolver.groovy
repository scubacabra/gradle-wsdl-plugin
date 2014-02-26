package com.jacobo.gradle.plugins.resolve

interface ChildrenPathResolver {

  /**
   * Resolve paths relative to a parent Dir.
   * Will get relative paths of files relative to a directory
   * @param parentDir the directory to get relative paths relative to
   * @param files Set of files that contain paths
   * @return list of relative paths
   */
  public List resolvePaths(File parentDir, Set<File> files)
}