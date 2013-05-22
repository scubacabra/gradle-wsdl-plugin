package com.jacobo.gradle.plugins

import com.jacobo.gradle.plugins.extension.WsdlPluginExtension

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
import com.jacobo.gradle.plugins.tasks.ResolveWsdlDependenciesTask

/**
 * @author djmijares
 * Created: Mon Jan 07 18:08:42 EST 2013
 */
class WsdlPlugin implements Plugin<Project> {
  static final String WSDL_PLUGIN_TASK_GROUP = 'parse'
  static final String WSDL_PLUGIN_PARSE_WSDL_TASK = 'parseWsdl'
  static final String WSDL_PLUGIN_WSDL_NAME_TASK = 'wsdlName'
  static final String WSDL_PLUGIN_RESOLVE_WSDL_DEPENDENCIES_TASK = 'resolveWsdlDependencies'
  static final String WSDL_PLUGIN_WSDL_RESOLVE_TASK = 'wsdlResolve'
  static final String WSDL_CONFIGURATION_NAME = 'jaxws'

  static final Logger log = Logging.getLogger(WsdlPlugin.class)

  private WsdlPluginExtension extension

   void apply (Project project) {
     project.plugins.apply(JavaPlugin)
     project.plugins.apply(WarPlugin)
     configureWsdlExtension(project)
     configureWsdlConfiguration(project)
     def nameTask = configureWsdlNameTask(project)
     Task pwt = configureParseWsdlTask(project, nameTask)
     def dependenciesTask = configureResolveWsdlDependenciesTask(project, nameTask)
     def resolverTask = configureWsdlResolverTask(project, dependenciesTask)
     configureWarTask(project, resolverTask)
   }

   private void configureWsdlExtension(final Project project) { 
     extension = project.extensions.create("wsdl", WsdlPluginExtension, project)
     extension.with { 
       wsdlDirectory = new File(project.rootDir, "wsdl")
       wsImport.sourceDestinationDirectory = "src/main/java"
       wsImport.episodeDirectory = new File(project.rootDir, "schema/episodes")
       wsdlWar.wsdlWarDir = "wsdl"
       wsdlWar.schemaWarDir = "schema"
       wsdlWar.resolvedWebServiceDir = project.file(new File(project.buildDir, "web-service"))
       wsdlWar.resolvedWsdlDir = project.file(new File(project.wsdl.resolvedWebServiceDir, "wsdl"))
       wsdlWar.resolvedSchemaDir = project.file(new File(project.wsdl.resolvedWebServiceDir, "schema"))
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
     pwt.conventionMapping.wsdl         = { project.wsdl.wsdlPath }
     pwt.conventionMapping.destination  = { project.file(new File(project.projectDir, project.wsdl.wsImport.sourceDestinationDirectory)) }
     pwt.conventionMapping.episode      = { project.wsdl.wsImport.episodeDirectory }
     pwt.conventionMapping.episodes     = { project.wsdl.wsImport.episodes }
     pwt.conventionMapping.target       = { project.wsdl.wsImport.target }
     pwt.conventionMapping.wsdlLocation = { project.wsdl.wsImport.wsdlLocation }
     pwt.conventionMapping.verbose      = { project.wsdl.wsImport.verbose }
     pwt.conventionMapping.keep         = { project.wsdl.wsImport.keep }
     pwt.conventionMapping.xnocompile   = { project.wsdl.wsImport.xnocompile }
     pwt.conventionMapping.fork         = { project.wsdl.wsImport.fork }
     pwt.conventionMapping.xdebug       = { project.wsdl.wsImport.xdebug }     
     return pwt
   }

   private Task configureWsdlNameTask(final Project project) { 
     Task wnt = project.tasks.add(WSDL_PLUGIN_WSDL_NAME_TASK, WsdlNameTask)
     wnt.description = "find the wsdl File name from the web service sub project name, as per the convention"
     wnt.group = WSDL_PLUGIN_TASK_GROUP
     wnt.conventionMapping.projectName = { project.name }
     wnt.conventionMapping.wsdlDirectory = { project.wsdl.wsdlDirectory }
     return wnt
   }

   private Task configureResolveWsdlDependenciesTask(final Project project, Task wsdlNameTask) { 
     Task resolveDeps = project.tasks.add(WSDL_PLUGIN_RESOLVE_WDSL_DEPENDENCIES_TASK, ResolveWsdlDependenciesTask)
     resolveDeps.description = "determine all the wsdl dependencies, expected via, import/include statements"
     resolveDeps.group = WSDL_PLUGIN_TASK_GROUP
     resolveDeps.dependsOn(wsdlNameTask)
     resolveDeps.conventionMapping.wsdl = { project.wsdl.wsdlPath }
     return resolveDeps
   }

   private Task configureWsdlResolverTask(final Project project, Task resolveWsdlDependenciesTask) {
     Task wrt = project.tasks.add(WSDL_PLUGIN_WSDL_RESOLVE_TASK, WsdlResolverTask)
     wrt.description = "gather all the wsdl dependencies (xsd's, wsdl's) and create a relative file list to be populated in the war"
     wrt.group = WSDL_PLUGIN_TASK_GROUP
     wrt.dependsOn(resolveWsdlDependenciesTask)
     wrt.conventionMapping.rootDir = { project.rootDir }
     wrt.conventionMapping.resolvedWebServicesDir = { project.wsdl.wsdlWar.resolvedWebServiceDir }
     wrt.conventionMapping.resolvedWsdlDir = { project.wsdl.wsdlWar.resolvedWsdlDir }
     wrt.conventionMapping.resolvedSchemaDir = { project.wsdl.wsdlWar.resolvedSchemaDir }
     return wrt
   }


   private void configureWarTask(final Project project, Task wsdlDependencyResolver) {
     Task oldWar = project.tasks.getByName('war')
     Task wsdlWar = project.tasks.replace(WarPlugin.WAR_TASK_NAME, WsdlWarTask)
     wsdlWar.group = oldWar.group
     wsdlWar.description = oldWar.description + " Also bundles the xsd and wsdl files this service depends on"
     wsdlWar.dependsOn(wsdlDependencyResolver)
     wsdlWar.conventionMapping.wsdlFolder    = { project.wsdl.wsdlWar.wsdlWarDir }
     wsdlWar.conventionMapping.schemaFolder  = { project.wsdl.wsdlWar.schemaWarDir }
     wsdlWar.conventionMapping.wsdl          = { project.wsdl.wsdlWar.resolvedWsdlDir }
     wsdlWar.conventionMapping.schema        = { project.wsdl.wsdlWar.resolvedSchemaDir }
     project.build.dependsOn(wsdlWar)
   }
}