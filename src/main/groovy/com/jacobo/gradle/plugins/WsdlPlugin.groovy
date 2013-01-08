package com.jacobo.gradle.plugins

import org.gradle.api.Project
import org.gradle.api.Plugin

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

import com.jacobo.gradle.plugins.tasks.WsdlNameTask
/**
 * @author djmijares
 * Created: Mon Jan 07 18:08:42 EST 2013
 */
class WsdlPlugin implements Plugin<Project> {
  static final String WSDL_PLUGIN_TASK_GROUP = 'parse'
  static final String WSDL_PLUGIN_PARSE_WSDL_TASK = 'parseWsdl'
  static final String WSDL_PLUGIN_WSDL_NAME_TASK = 'wsdlName'
  static final String WSDL_CONFIGURATION_NAME = 'jaxws'

  static final Logger log = Logging.getLogger(WsdlPlugin.class)

  private WsdlExtension extension

   void apply (Project project) {
      // add your plugin tasks here.
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

   private void configureParseWsdlTask(final Project project) { 
   
   }

   private WsdlNameTask configureWsdlNameTask(final Project project) { 
     WsdlNameTask wnt = project.tasks.add(WSDL_PLUGIN_WSDL_NAME_TASK, WsdlNameTask)
     wnt.description = "find the wsdl File name from the web service sub project name, as per the convention"
     wnt.group = WSDL_PLUGIN_TASK_GROUP
     return wnt
   }

   
}