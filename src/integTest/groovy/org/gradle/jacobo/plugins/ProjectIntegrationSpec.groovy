package org.gradle.jacobo.plugins

import org.gradle.api.plugins.WarPlugin
import org.gradle.testfixtures.ProjectBuilder

import org.gradle.jacobo.plugins.WsdlPlugin
import org.gradle.jacobo.plugins.task.ConvertProjNameToWsdl
import org.gradle.jacobo.plugins.task.WsdlResolveDependencies
import org.gradle.jacobo.plugins.task.WsdlWar
import org.gradle.jacobo.plugins.task.WsdlWsImport
import org.gradle.jacobo.plugins.ProjectTaskSpecification

class ProjectIntegrationSpec extends ProjectTaskSpecification {
  // Task names
  def convertTaskName = WsdlPlugin.CONVERSION_TASK_NAME
  def resolveTaskName = WsdlPlugin.RESOLVE_DEPENDENCIES_TASK_NAME
  def warTaskName     = WarPlugin.WAR_TASK_NAME
  def wsImportTaskName = WsdlPlugin.WSIMPORT_TASK_NAME

  // tasks
  def convertTask
  def resolveTask
  def warTask
  def wsImportTask
  
  // root project to add sub project too, like a real situation would consist of
  def rootProject

  def setRootProject(def rootDir) {
    rootProject = ProjectBuilder.builder().withProjectDir(rootDir).build()
  }
  
  def setSubProject(def rootProject, def projectName, def plugin) {
    project = ProjectBuilder.builder().withName(projectName).withParent(rootProject).build()
    project.apply(plugin: plugin)
  }

  def setupProjectTasks() {
    convertTask = project.tasks[convertTaskName] as ConvertProjNameToWsdl
    resolveTask = project.tasks[resolveTaskName] as WsdlResolveDependencies
    warTask     = project.tasks[warTaskName] as WsdlWar
    wsImportTask = project.tasks[wsImportTaskName] as WsdlWsImport
  }
}