package com.jacobo.gradle.plugins.copy

import groovy.util.AntBuilder

interface CopyDependencies {

  /**
   * Copies dependencies via an #AntBuilder instances copy task
   * @param ant AntBuilder to use on the copy task
   * @param toDir copy to this directory
   * @param fromDir copy from this directory
   * @param includeFiles file to include (comma, or space separated list of file patterns to include
   *   i.e "a.txt,b.txt,c.txt" OR "a.txt b.txt c.txt"
   */
  public void copy(AntBuilder ant, File toDir, File fromDir, String includeFiles)
}