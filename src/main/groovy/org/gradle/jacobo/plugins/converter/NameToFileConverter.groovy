package org.gradle.jacobo.plugins.converter

/**
 *  Converts a wsdl projects Name to the corresponding WSDL file to use.
 */
interface NameToFileConverter extends NameConverter {
  
  /**
   * Converts a name (String) to a file that resides in the directory, applying
   *   certain rules to the conversion.
   *   
   * @param name  string to convert
   * @param directory  where the file resides
   * @param nameRules  map of rules to apply when converting
   * @return corresponding WSDL file
   */
  public File convert(String name, File directory, Map nameRules)
  
  /**
   * Converts a string to another string, applying certain rules during conversion.
   * 
   * @param name  string to convert
   * @param nameRules  map of rules to apply when converting the string
   * @return new string with applied rules to convert
   */
  public String convert(String name, Map nameRules)
}