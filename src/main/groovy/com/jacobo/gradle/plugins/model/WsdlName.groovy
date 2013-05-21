package com.jacobo.gradle.plugins.model

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

import org.gradle.api.GradleException

/**
 * Class that takes the project name of the web service project and parses it to generate the associated wsdl file name per the convention
 * @author djmijares
 * Created: Mon Jan 07 18:08:42 EST 2013
 */
class WsdlName { 
   static final Logger log = Logging.getLogger(WsdlName.class) 

    /**
     * wsdl file name
     */
   def wsdlName

    /**
     * converts the #projectName into the #wsdlName
     * @param projectName  String of the web service project name to convert
     * @return #wsdlName
     * @throws GradleException
     */
   public String findWsdlFileName(String projectName) throws GradleException { 
    if (!projectName.contains("-ws")) {
      throw new GradleException("${projectName} is not conforming to the convention, needs to be suffixed with '-ws' at the end at the very least.  Double check")
    }
    wsdlName = removeSuffix(projectName)
    wsdlName = convertDashedToCamelCase(wsdlName)
    wsdlName = appendService(wsdlName)
    wsdlName = capitalizeFirstLetter(wsdlName)
    return wsdlName
  }

    /**
     * Removes the '-ws' suffix on the project
     * @param projectName
     * @return
     */
  private String removeSuffix(String projectName) { 
    return projectName.replaceFirst(/-ws/, "")
  }

    /**
     * Converts #dashedName to camel case Name
     * dashed-name => dashedName
     * @param dashedName
     * @return
     */
  private String convertDashedToCamelCase(String dashedName) { 
    dashedName.replaceAll(/-(\w)/) { fullMatch, firstCharacter -> 
      firstCharacter.toUpperCase()
    }
  }

    /**
     * Per this plugins convention, wsdl files will have a "Service" appended to them.
     * Append this to #name
     * @param name
     * @return
     */
  private String appendService(String name) { 
    return name + "Service"
  }

    /**
     * Per convention, wsdl files have their first letter capitalized.
     * Capitalizes the first letter
     * @param name
     * @return
     */
  private String capitalizeFirstLetter(String name) { 
    name.replaceFirst(/^./) { it.toUpperCase() }
  }

}