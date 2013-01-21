package com.jacobo.gradle.plugins

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.Task

import org.gradle.api.plugins.WarPlugin
import org.gradle.api.tasks.bundling.War

import org.gradle.api.plugins.JavaPlugin

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

import com.jacobo.gradle.plugins.tasks.WsdlNameTask
import com.jacobo.gradle.plugins.tasks.WsdlWarTask
import com.jacobo.gradle.plugins.tasks.ParseWsdlTask
import com.jacobo.gradle.plugins.tasks.WsdlResolverTask

/**
 * @author djmijares
 * Created: Mon Jan 07 18:08:42 EST 2013
 */
class WsdlPlugin implements Plugin<Project> {
  static final String WSDL_PLUGIN_TASK_GROUP = 'parse'
  static final String WSDL_PLUGIN_PARSE_WSDL_TASK = 'parseWsdl'
  static final String WSDL_PLUGIN_WSDL_NAME_TASK = 'wsdlName'
  static final String WSDL_PLUGIN_WSDL_RESOLVE_TASK = 'wsdlResolve'
  static final String WSDL_CONFIGURATION_NAME = 'jaxws'

  static final Logger log = Logging.getLogger(WsdlPlugin.class)

  private WsdlExtension extension

   void apply (Project project) {
     project.plugins.apply(JavaPlugin)
     project.plugins.apply(WarPlugin)
     configureWsdlExtension(project)
     configureWsdlConfiguration(project)
     def nameTask = configureWsdlNameTask(project)
     Task pwt = configureParseWsdlTask(project, nameTask)
     def resolverTask = configureWsdlResolverTask(project, nameTask)
     configureWarTask(project, resolverTask)
   }

   private void configureWsdlExtension(final Project project) { 
     extension = project.extensions.create("wsdl", WsdlExtension, project)
     extension.with { 
       wsdlDirectory = new File(project.rootDir, "wsdl")
       sourceDestinationDirectory = "src/main/java"
       episodeDirectory = new File(project.rootDir, "schema/episodes")
     }
   }

   private void configureWsdlConfiguration(final Project project) { 
     project.configurations.add(WSDL_CONFIGURATION_NAME) { 
       visible = true
       transitive = true
       description = "The JAXWS libraries to be used for parsing the WSDL"
     }
   }

   private Task configureParseWsdlTask(final Project project, Task wsdlNameTask) { 
     Task pwt = project.tasks.add(WSDL_PLUGIN_PARSE_WSDL_TASK, ParseWsdlTask)
     pwt.description = "parse the wsdl with jaxws and wsimport"
     pwt.group = WSDL_PLUGIN_TASK_GROUP
     pwt.dependsOn(wsdlNameTask)
     pwt.destinationDirectory = new File(project.wsdl.sourceDestinationDirectory)
     //     pwt.doLast { wsdl = project.wsdl.wsdlPath }
     return pwt

   }

   private Task configureWsdlNameTask(final Project project) { 
     Task wnt = project.tasks.add(WSDL_PLUGIN_WSDL_NAME_TASK, WsdlNameTask)
     wnt.description = "find the wsdl File name from the web service sub project name, as per the convention"
     wnt.group = WSDL_PLUGIN_TASK_GROUP
     return wnt
   }

   private Task configureWsdlResolverTask(final Project project, Task wsdlNameTask) {
     Task wrt = project.tasks.add(WSDL_PLUGIN_WSDL_RESOLVE_TASK, WsdlResolverTask)
     wrt.description = "gather all the wsdl dependencies (xsd's, wsdl's) and create a relative file list to be populated in the war"
     wrt.group = WSDL_PLUGIN_TASK_GROUP
     wrt.dependsOn(project.tasks.getByName('classes'))
     wrt.dependsOn(wsdlNameTask)
     return wrt
   }

   private void configureWarTask(final Project project, Task wsdlResolverTask) {
     Task war = project.tasks.replace(WarPlugin.WAR_TASK_NAME, WsdlWarTask)
     war.dependsOn(wsdlResolverTask)
   }
}