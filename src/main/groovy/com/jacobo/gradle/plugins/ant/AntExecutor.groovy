package com.jacobo.gradle.plugins.ant

import groovy.util.AntBuilder

interface AntExecutor {
  
  /**
   * Executes the ant task the implementation is executing.
   * @param ant AntBuilder to build and execute
   * @param arguments to configure antBuilder with
   */
  public void execute(AntBuilder ant, Map<String, Object> arguments)
}