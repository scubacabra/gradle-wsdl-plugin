package com.jacobo.gradle.plugins.extension

import org.gradle.api.Project

/**
 * Contains @see WsdlPlugin default settings
 * @author djmijares
 * Created: Tue Dec 04 09:01:34 EST 2012
 */
class WsdlPluginExtension {

  private Project project

  /**
   * wsimport default settings for now
   */
  WsImportExtension wsImport = new WsImportExtension()

  /**
   * wsdl War default settings for now
   */
  WsdlWarExtension wsdlWar = new WsdlWarExtension()

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
   * Absolute Files of all wsdl dependencies
   * @note even having a wsdl with no dependencies, the Web Service is still dependnent on it's sole WSDL
   */
  List wsdlDependencies

  /**
   * War files grouped by common folder
   * @note again, at the minimum, this has at least one object, the web service must depend on it's WSDL
   */
  List<GroupedWsdlWarFiles> warFiles

  WsdlPluginExtension(Project project) {
    this.project = project
  }

  void setWsdlPath() { 
    this.wsdlPath = new File(wsdlDirectory, wsdlFileName + ".wsdl")
  }
}