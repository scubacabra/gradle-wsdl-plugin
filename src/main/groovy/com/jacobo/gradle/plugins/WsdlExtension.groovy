package com.jacobo.gradle.plugins

import org.gradle.api.Project

/**
 * @author djmijares
 * Created: Tue Dec 04 09:01:34 EST 2012
 */
class WsdlExtension { 

  private Project project

    /**
     *  Directory where to find Wsdl files with .wsdl extensions
     */
  File wsdlDirectory

    /**
     *  wsdl file name, no path
     */
  String wsdlFileName

    /**
     *  wsdl file, full path
     */
  File wsdlPath

    /**
     * argument for wsimport ant task in @see ParseWsdlTask
     * destination directory for the generated java code
     */
  String sourceDestinationDirectory

    /**
     * argument for wsimport ant task in @see ParseWsdlTask
     */
  boolean verbose = true

    /**
     * argument for wsimport ant task in @see ParseWsdlTask
     */
  boolean keep = true

    /**
     * argument for wsimport ant task in @see ParseWsdlTask
     */
  boolean xnocompile = true

    /**
     * argument for wsimport ant task in @see ParseWsdlTask
     */
  boolean fork = false

    /**
     * argument for wsimport ant task in @see ParseWsdlTask
     */
  boolean xdebug = false

    /**
     * argument for wsimport ant task in @see ParseWsdlTask
     */
  String target = "2.1"

    /**
     * argument for wsimport ant task in @see ParseWsdlTask
     */
  String wsdlLocation = "FILL_IN_BY_SERVER"

    /**
     * argument for wsimport ant task in @see ParseWsdlTask
     */
  File episodeDirectory

    /**
     * argument for wsimport ant task in @see ParseWsdlTask
     */
  List episodes = []

    /**
     * ???
     */
  List resolved

    /**
     *  WAR defaults
     */
  String wsdlWarDir

    /**
     *  WAR defaults
     */
  String schemaWarDir

    /**
     *  resolved Output Dir for web service War Task @see WsdlWarTask
     */
  File resolvedWebServiceDir

    /**
     *  resolved Output Dir for web service War Task @see WsdlWarTask
     */
  File resolvedWsdlDir

    /**
     *  resolved Output Dir for web service War Task @see WsdlWarTask
     */
  File resolvedSchemaDir

  WsdlExtension(Project project) { 
    this.project = project
  }

  void setWsdlPath() { 
    this.wsdlPath = new File(wsdlDirectory, wsdlFileName + ".wsdl")
  }
}