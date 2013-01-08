package com.jacobo.gradle.plugins

import org.gradle.api.Project

/**
 * @author djmijares
 * Created: Tue Dec 04 09:01:34 EST 2012
 */
class WsdlExtension { 
  private Project project

  String wsdlDirectory
  String wsdlFileName
  String wsdlParseDestinationDirectory
  String episodesDirectory
  
  List episodes = []

  WsdlExtension(Project project) { 
    this.project = project
  }

}