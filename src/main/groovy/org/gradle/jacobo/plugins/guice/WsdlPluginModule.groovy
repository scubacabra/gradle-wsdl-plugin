package org.gradle.jacobo.plugins.guice

import com.google.inject.AbstractModule

import org.gradle.jacobo.plugins.resolver.DependencyResolver
import org.gradle.jacobo.plugins.resolver.WsdlDependencyResolver

class WsdlPluginModule extends AbstractModule {
  
  @Override
  protected void configure() {
    bind(DependencyResolver.class).to(WsdlDependencyResolver.class)
  }
}