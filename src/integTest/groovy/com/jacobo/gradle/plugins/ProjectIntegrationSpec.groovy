package com.jacobo.gradle.plugins

import org.gradle.api.plugins.WarPlugin
import org.gradle.testfixtures.ProjectBuilder

import com.jacobo.gradle.plugins.WsdlPlugin
import com.jacobo.gradle.plugins.tasks.ConvertProjNameToWsdl
import com.jacobo.gradle.plugins.tasks.WsdlResolveDependencies
import com.jacobo.gradle.plugins.tasks.WsdlWar
import com.jacobo.gradle.plugins.ProjectTaskSpecification

class ProjectIntegrationSpec extends ProjectTaskSpecification {
  // Task names
  def convertTaskName = WsdlPlugin.CONVERSION_TASK_NAME
  def resolveTaskName = WsdlPlugin.RESOLVE_DEPENDENCIES_TASK_NAME
  def warTaskName     = WarPlugin.WAR_TASK_NAME

  // tasks
  def convertTask
  def resolveTask
  def warTask
  
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
  }
}