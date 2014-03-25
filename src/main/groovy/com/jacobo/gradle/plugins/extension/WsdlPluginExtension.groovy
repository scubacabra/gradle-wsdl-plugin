package com.jacobo.gradle.plugins.extension

import org.gradle.api.Project

import org.gradle.api.file.FileCollection

/**
 * Contains @see WsdlPlugin default settings
 * @author jacobono
 * Created: Tue Dec 04 09:01:34 EST 2012
 */
class WsdlPluginExtension {

  private Project project

  /**
   * wsimport default settings for now
   */
  WsImportExtension wsImport = new WsImportExtension()

  /**
   *  wsdl folder name, under root Directory
   */
  String wsdlFolder

  /**
   *  schema folder name, under root Directory
   */
  String schemaFolder

  /**
   *  episodes folder name, under root Directory
   */
  String episodeFolder

  /**
   * Name rules to convert project name to an appropriate wsdl name
   * For example, "-dm" -> "DataManagement", in project-dm-name, becomes "ProjectDataManagementName"
   */
  Map<String, String> nameRules

  /**
   * projects wsdl file
   */
  File wsdlFile

  /**
   * Wsdl dependencies files (including projects WSDL itself)
   */
  FileCollection wsdlDependencies

  /**
   * List of episodes files (under the epiosde Folder) to bind into the wsimport task
   * Using these files means that already generated schema artifacts previously done with xjc.
   * examples: ["some-episode.episode", "another.episode"]
   */
  List episodes

  /**
   * Constructor
   */
  WsdlPluginExtension(Project project) {
    this.project = project
  }
}