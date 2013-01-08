package com.jacobo.gradle.plugins

import org.gradle.api.Project

/**
 * @author djmijares
 * Created: Tue Dec 04 09:01:34 EST 2012
 */
class WsdlExtension { 

  private Project project

  File wsdlDirectory
  String wsdlFileName
  File wsdlPath

  //args to the wsimport
  String sourceDestinationDirectory
  boolean verbose = true
  boolean keep = true
  boolean xnocompile = true
  boolean fork = false
  boolean xdebug = false
  String target = "2.2"
  String wsdlLocation = "FILL_IN_BY_SERVER"

  File episodeDirectory  
  List episodes = []

  WsdlExtension(Project project) { 
    this.project = project
  }

  void setWsdlPath() { 
    this.wsdlPath = new File(wsdlDirectory, wsdlFileName)
  }
}