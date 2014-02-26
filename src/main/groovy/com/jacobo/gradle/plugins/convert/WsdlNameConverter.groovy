package com.jacobo.gradle.plugins.convert

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger
import org.gradle.api.GradleException

class WsdlNameConverter implements ProjectNameConverter {
  static final Logger log = Logging.getLogger(WsdlNameConverter.class)
  
  /**
   * Project Name is converted to the  WSDL name it is associated with
   * @param projectName - the project name
   * @return converted project name
   */
  public String convert(String projectName) {
    if (!projectName.contains("-ws")) { 
      def errorStrings = ["$projectName does not conform to the convention -- ",
			  "MUST be suffixed with a '-ws'"]
      throw new GradleException(errorStrings.sum())
    }
    
    log.debug("Converting '{}' to its WSDL name", projectName)
    def sansSuffix = projectName[0..-4]
    def camelCase = sansSuffix.replaceAll(/-(\w)/) { 
      fullMatch, firstCharacter ->
	firstCharacter.toUpperCase()
    }
    def capFirstLetter = camelCase.replaceFirst(/^./) { it.toUpperCase() }
    return "${capFirstLetter}Service"
  }
}