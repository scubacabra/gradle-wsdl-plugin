package com.jacobo.gradle.plugins.util

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

import org.gradle.api.GradleException

/**
 * Class that takes the project name of the web service project and parses it to generate the associated wsdl file name per the convention
 * @author djmijares
 * Created: Mon Jan 07 18:08:42 EST 2013
 */
class WsdlNameHelper {
   static final Logger log = Logging.getLogger(WsdlNameHelper.class)

    /**
     * converts the #projectName into the #wsdlName
     * @param projectName  String of the web service project name to convert
     * @return #wsdlName
     * @throws GradleException
     */
   public static String generateWsdlName(String projectName) throws GradleException { 
    if (!projectName.contains("-ws")) {
      throw new GradleException("${projectName} is not conforming to the convention, needs to be suffixed with '-ws' at the end at the very least.  Double check")
    }
    def wsdlName = removeSuffix(projectName)
    wsdlName = convertDashedToCamelCase(wsdlName)
    wsdlName = appendService(wsdlName)
    wsdlName = capitalizeFirstLetter(wsdlName)
    return wsdlName
  }

    /**
     * Removes the '-ws' suffix on the project
     * @param projectName
     * @return procject name sans -ws suffix
     */
  private static String removeSuffix(String projectName) { 
    return projectName.replaceFirst(/-ws/, "")
  }

    /**
     * Converts #dashedName to camel case Name
     * dashed-name => dashedName
     * @param dashedName
     * @return
     */
  private static String convertDashedToCamelCase(String dashedName) { 
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
  private static String appendService(String name) { 
    return name + "Service"
  }

    /**
     * Per convention, wsdl files have their first letter capitalized.
     * Capitalizes the first letter
     * @param name
     * @return
     */
  private static String capitalizeFirstLetter(String name) { 
    name.replaceFirst(/^./) { it.toUpperCase() }
  }

}