package com.jacobo.gradle.plugins.util

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

import org.gradle.api.GradleException

/**
 * Bunch of static methods that operate on a project name to get the result of the correct wsdl name
 * @author jacobono
 * Created: Mon Jan 07 18:08:42 EST 2013
 */
class WsdlNameHelper {
   static final Logger log = Logging.getLogger(WsdlNameHelper.class)

    /**
     * converts the #projectName into the #wsdlName
     * @param projectName  String of the web service project name to convert
     * @param nameRules map of name rules with keys present in project name that are transformed into values, @note default is null
     * @return #wsdlName
     * @throws GradleException
     */
   public static String generateWsdlName(String projectName, Map nameRules = null) throws GradleException { 

    if (!projectName.contains("-ws"))
      throw new GradleException("${projectName} is not conforming to the convention, needs to be suffixed with '-ws' at the end at the very least.  Double check")

    log.debug("Generating the WSDL name for project '{}'", projectName)
    def wsdlName = removeSuffix(projectName)

    if(nameRules) // if there are any name rules, process them here
      wsdlName = applyNameRules(wsdlName, nameRules)

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

    /**
     * apply Name rules to the wsdlName
     * @param wsdlName Name that the nameRules are applied to
     * @param nameRules rules to apply to the wsdl name
     * @return new rule transformed string
     */
  private static String applyNameRules(String wsdlName,Map nameRules) { 
    nameRules.each { abbrev, expansion ->
      log.debug("Applying rule '{}' -> '{}' on '{}'", abbrev, expansion, wsdlName)
      wsdlName = wsdlName.replace(abbrev, expansion)
      log.debug("Applying rule yields name of '{}'", wsdlName)
    }
    return wsdlName
  }
}