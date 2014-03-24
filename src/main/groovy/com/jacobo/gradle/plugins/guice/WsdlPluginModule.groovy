package com.jacobo.gradle.plugins.guice

import com.google.inject.AbstractModule

import com.jacobo.gradle.plugins.resolve.DependencyResolver
import com.jacobo.gradle.plugins.resolve.WsdlDependencyResolver

class WsdlPluginModule extends AbstractModule {
  
  @Override
  protected void configure() {
    bind(DependencyResolver.class).to(WsdlDependencyResolver.class)
  }
}