package com.jacobo.gradle.plugins

import org.gradle.api.Project
import org.gradle.api.Plugin

/**
 * @author djmijares
 * Created: Mon Jan 07 18:08:42 EST 2013
 */
class WsdlPlugin implements Plugin<Project> {
   void apply (Project project) {
      project.convention.plugins.WsdlPlugin = new WsdlPluginConvention()
      
      // add your plugin tasks here.
   }
}