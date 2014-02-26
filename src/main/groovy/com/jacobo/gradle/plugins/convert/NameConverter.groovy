package com.jacobo.gradle.plugins.convert

interface ProjectNameConverter {
  
  /**
   * Project Name is converted in a specific way to an appropriate string
   * @param projectName - the project name
   * @return converted project name
   */
  public String convert(String projectName)
}