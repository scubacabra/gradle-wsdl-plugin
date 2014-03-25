package com.jacobo.gradle.plugins.convert

interface NameToFileConverter extends NameConverter {
  
  /**
   * Converts a name to a file that resides in the directory, applying
   *   user input name rules
   * @param name name to convert
   * @param directory directory file resides in
   * @param nameRules map of name rules, containing abbreviations to find
   *   the expansion to convert them to
   */
  public File convert(String name, File directory, Map nameRules)
  
  /**
   * Converts a name to a file, applying user input name rules
   * @param name name to convert
   * @param nameRules map of name rules, containing abbreviations to find
   *   the expansion to convert them to
   */
  public String convert(String name, Map nameRules)
}