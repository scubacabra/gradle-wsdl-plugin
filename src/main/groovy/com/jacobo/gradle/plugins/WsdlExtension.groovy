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
  String wsdlPath

  //args to the wsimport
  String sourceDestinationDirectory
  boolean verbose = true
  boolean keep = true
  boolean xnocompile = true
  boolean fork = false
  String target = null
  String wsdlLocation = "FILL_IN_BY_SERVER"

  String episodeDirectory  
  List episodes = []

  WsdlExtension(Project project) { 
    this.project = project
  }

}