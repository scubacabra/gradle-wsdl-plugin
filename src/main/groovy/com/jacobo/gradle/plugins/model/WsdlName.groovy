package com.jacobo.gradle.plugins.model

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

import org.gradle.api.GradleException

/**
 * @author djmijares
 * Created: Mon Jan 07 18:08:42 EST 2013
 */
class WsdlName { 
   static final Logger log = Logging.getLogger(WsdlName.class) 

   def wsdlName

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
  
  private String removeSuffix(String projectName) { 
    return projectName.replaceFirst(/-ws/, "")
  }

  private String convertDashedToCamelCase(String dashedName) { 
    dashedName.replaceAll(/-(\w)/) { fullMatch, firstCharacter -> 
      firstCharacter.toUpperCase()
    }
  }

  private String appendService(String name) { 
    return name + "Service"
  }

  private String capitalizeFirstLetter(String name) { 
    name.replaceFirst(/^./) { it.toUpperCase() }
  }

}