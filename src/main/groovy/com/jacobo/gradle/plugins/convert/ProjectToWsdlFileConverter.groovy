package com.jacobo.gradle.plugins.convert

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger
import org.gradle.api.GradleException

class ProjectToWsdlFileConverter implements NameToFileConverter {
  static final Logger log = Logging.getLogger(ProjectToWsdlFileConverter.class)
  
  /**
   * Project Name is converted to the  WSDL name it is associated with
   * @param projectName - the project name
   * @return converted project name
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
   * Converts wsdl project name to wsdl file residing in directory
   * @param projectName name to convert
   * @param wsdlDirectory directory file resides in
   */
  @Override
  public File convert(String projectName, File wsdlDirectory) {
    def wsdlName = convert(projectName)
    def wsdlFile = new File(wsdlDirectory, "${wsdlName}.wsdl")
    if (wsdlFile.exists()) return wsdlFile
    def exceptionMessage = ["File ${wsdlName}.wsdl does not exist at at default ",
			    "location of '$wsdlDirectory'"]
    throw new GradleException(exceptionMessage.sum())
  }
}