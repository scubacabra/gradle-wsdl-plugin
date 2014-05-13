package org.gradle.jacobo.plugins.converter

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger
import org.gradle.api.GradleException

/**
 *  Converts a wsdl projects Name to its corresponding WSDL file.
 */
class ProjectToWsdlFileConverter implements NameToFileConverter {
  static final Logger log = Logging.getLogger(ProjectToWsdlFileConverter.class)
  
  /**
   * Converts a project Name that is possibly expanded with name Rules, to
   * the projects corresponding WSDL file name (sans extension).
   * <p>
   * Project name must conform to specific conventions, else errors are thrown.
   * <ul>
   * <li> contains suffix {@code -ws}
   * <li> all lower case, words separated by hyphen i.e. {@code hello-you-ws}
   * </ul>
   * <p>
   * 
   * @param projectName  project name with possible nameRules applied
   * @return WSDL file name (sans extension)
   * @throws {@link org.gradle.api.GradleException} when conventions aren't present
   */
  @Override
  public String convert(String projectName) {
    if (!projectName.contains("-ws")) { 
      def errorStrings = ["$projectName does not conform to the convention -- ",
			  "MUST be suffixed with a '-ws'"]
      throw new GradleException(errorStrings.sum())
    }

    log.debug("converting '{}' to its WSDL name", projectName)
    def sansSuffix = projectName[0..-4] // remvoe suffix

    // dashed to camel case
    def camelCase = sansSuffix.replaceAll(/-(\w)/) { 
      fullMatch, firstCharacter ->
	firstCharacter.toUpperCase()
    }

    // capitalize first letter
    def wsdlName = camelCase.replaceFirst(/^./) { it.toUpperCase() }
    log.debug("converted '{}' ==> '{}'", projectName, wsdlName)
    return "${wsdlName}Service"
  }

  /**
   * Converts a name (String) to a file that resides in the directory, applying
   *   certain rules to the conversion.
   *   
   * @param name  string to convert
   * @param directory  where the file resides
   * @param nameRules  map of rules to apply when converting
   * @return corresponding WSDL file
   * @throws {@link org.gradle.api.GradleException} when WSDL file does not exist
   */
  @Override
  public File convert(String projectName, File wsdlDirectory, Map nameRules) {
    def wsdlName = convert(projectName, nameRules)
    def wsdlFile = new File(wsdlDirectory, "${wsdlName}.wsdl")
    if (wsdlFile.exists()) return wsdlFile
    def exceptionMessage = ["File ${wsdlName}.wsdl does not exist at at default ",
			    "location of '$wsdlDirectory'"]
    throw new GradleException(exceptionMessage.sum())
  }

  /**
   * Converts a string to another string, applying certain rules during conversion.
   * 
   * @param name  string to convert
   * @param nameRules  map of rules to apply when converting the string
   * @return new string with applied rules to convert
   */
  @Override
  public String convert(String projectName, Map nameRules) {
    nameRules.each { abbrev, expansion ->
      log.debug("Applying rule '{}' -> '{}' on '{}'", abbrev, expansion, projectName)
      projectName = projectName.replace(abbrev, expansion)
    }
    return convert(projectName)
  }
}