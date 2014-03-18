package com.jacobo.gradle.plugins

import com.jacobo.gradle.plugins.extension.WsdlPluginExtension
import com.jacobo.gradle.plugins.extension.WsImportExtension

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.Task

import org.gradle.api.plugins.WarPlugin
import org.gradle.api.tasks.bundling.War

import org.gradle.api.plugins.JavaPlugin

import com.jacobo.gradle.plugins.tasks.ConvertProjNameToWsdl
import com.jacobo.gradle.plugins.tasks.WsdlResolveDependencies
import com.jacobo.gradle.plugins.tasks.WsdlWar
import com.jacobo.gradle.plugins.tasks.WsdlWsImport

/**
 * @author djmijares
 * Created: Mon Jan 07 18:08:42 EST 2013
 */
class WsdlPlugin implements Plugin<Project> {
  static final String WSDL_PLUGIN_TASK_GROUP = 'jaxws'
  static final String WSIMPORT_TASK_NAME = 'wsimport'
  static final String CONVERSION_TASK_NAME = 'convertProjNameToWsdlFile'
  static final String RESOLVE_DEPENDENCIES_TASK_NAME = 'resolveWsdlDependencies'
  static final String WSDL_CONFIGURATION_NAME = 'jaxws'

  private WsdlPluginExtension extension

   void apply (Project project) {
     project.plugins.apply(JavaPlugin)
     project.plugins.apply(WarPlugin)
     configureWsdlExtension(project)
     configureWsdlConfiguration(project)
     configureWarTask(project)
     configureWsImportTask(project)
     def convertTask = configureConversionTask(project)
     def resolverTask = configureResolverTask(project, convertTask)
   }

   private void configureWsdlExtension(final Project project) { 
     extension = project.extensions.create("wsdl", WsdlPluginExtension, project)
     extension.with { 
       wsdlFolder		= "wsdl"
       schemaFolder		= "schema"
       episodeFolder		= "schema/episodes"
       webServiceCopyDir	= "web-service"
     }

     def wsimportExtension = project.wsdl.extensions.create("wsimport", WsImportExtension)
     wsimportExtension.with { 
       sourceDestinationDirectory	= "src/main/java"
       verbose				= true
       keep				= true
       xnocompile			= true
       fork				= false
       xdebug				= false
       target				= "2.1"
       wsdlLocation			= "FILL_IN_BY_SERVER"
     }
   }

   private void configureWsdlConfiguration(final Project project) { 
     project.configurations.create(WSDL_CONFIGURATION_NAME) { 
       visible		= true
       transitive	= true
       description	= "The JAXWS libraries to be used for parsing the WSDL"
     }
   }

   private configureConversionTask(final Project project) {
     Task convert = project.tasks.create(CONVERSION_TASK_NAME, ConvertProjNameToWsdl)
     convert.description = "convert the project name via convention to the projects wsdl file"
     convert.group = WSDL_PLUGIN_TASK_GROUP
     convert.conventionMapping.projectName	= { project.name }
     convert.conventionMapping.wsdlDirectory	= { project.file([project.rootDir.path, project.wsdl.wsdlFolder].join(File.separator)) }
     convert.conventionMapping.converter	= { new ProjectToWsdlFileConverter() }
     return convert
   }

   private configureResolverTask(final Project project, final Task convertTask) {
     Task resolver = project.tasks.create(RESOLVE_DEPENDENCIES_TASK_NAME,
					  WsdlResolveDependencies)
     resolver.description = "resolve all the wsdl dependencies for this project"
     resolver.group = WSDL_PLUGIN_TASK_GROUP
     resolver.dependsOn(convertTask)
     resolver.conventionMapping.dependencyResolver = { null }
     resolver.conventionMapping.wsdlFile = { project.wsdl.wsdlFile }
     return resolver
   }
     Task pwt = project.tasks.create(WSIMPORT_TASK_NAME, WsdlWsImport)
     pwt.description = "parse the wsdl with jaxws and wsimport"
     pwt.group = WSDL_PLUGIN_TASK_GROUP
     pwt.conventionMapping.destinationDirectory	= { project.file(new File(project.projectDir, project.wsdl.wsimport.sourceDestinationDirectory)) }
     pwt.conventionMapping.episodeDirectory     = { project.file(new File(project.rootDir, project.wsdl.episodeFolder)) }
   }

   private void configureWarTask(final Project project) {
     Task oldWar = project.tasks.getByName('war')
     Task wsdlWar = project.tasks.replace(WarPlugin.WAR_TASK_NAME, WsdlWar)
     wsdlWar.group = oldWar.group
     wsdlWar.description = oldWar.description + " Also bundles the xsd and wsdl files this service depends on"
     wsdlWar.conventionMapping.wsdlFolder             = { project.wsdl.wsdlFolder }
     wsdlWar.conventionMapping.schemaFolder           = { project.wsdl.schemaFolder }
     wsdlWar.conventionMapping.wsdlDirectory          = { project.file(new File(new File(project.buildDir, project.wsdl.webServiceCopyDir), project.wsdl.wsdlFolder)) }
     wsdlWar.conventionMapping.schemaDirectory        = { project.file(new File(new File(project.buildDir, project.wsdl.webServiceCopyDir), project.wsdl.schemaFolder)) }
     project.build.dependsOn(wsdlWar)
   }
}
