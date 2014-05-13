package org.gradle.jacobo.plugins.extension

import org.gradle.api.Project

import org.gradle.api.file.FileCollection

/**
 * {@code WsdlPlugin} default settings and conventions.
 */
class WsdlPluginExtension {

  private Project project

  /**
   * WsImport default ant task settings and conventions
   */
  WsImportExtension wsImport = new WsImportExtension()

  /**
   * Folder name (under {@link org.gradle.api.Project#getRootDir()}), containing the WSDL file.
   */
  String wsdlFolder

  /**
   * Folder name (under {@link org.gradle.api.Project#getRootDir()}), containing xsd files for wsdl
   * processing.
   */
  String schemaFolder

  /**
   * Folder name (under {@link org.gradle.api.Project#getRootDir()}), containing episodes files for
   * wsdl binding during the {@code wsimport} task.
   */
  String episodeFolder

  /**
   * Name rules to convert project name to an appropriate wsdl name.
   * <p>
   * For example, an entry {@code "-dm" -> "DataManagement"}, in
   * {@code project-dm-name}, yields an expansion to
   * {@code project-DataManagement-name}
   */
  Map<String, String> nameRules

  /**
   * WSDL file for this project.
   */
  File wsdlFile

  /**
   * Dependencies for this WSDL project (including the {@link #wsdlFile} itself).
   */
  FileCollection wsdlDependencies

  /**
   * User defined episodes files (names only, with extension) to bind into the
   * wsimport task.
   * <p>
   * Using these files means that already generated schema artifacts previously
   * done with xjc are bound at runtime instead of re-generated.
   * examples: {@code ["some-episode.episode", "another.episode"]}
   */
  List episodes

  WsdlPluginExtension(Project project) {
    this.project = project
  }
}