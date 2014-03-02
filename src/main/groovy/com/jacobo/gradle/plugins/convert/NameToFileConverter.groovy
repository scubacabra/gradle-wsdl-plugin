package com.jacobo.gradle.plugins.convert

interface NameToFileConverter extends NameConverter {
  
  /**
   * Converts a name to a file that resides in the directory
   * @param name name to convert
   * @param directory directory file resides in
   */
  public File convert(String name, File directory)
}