package org.gradle.jacobo.plugins.guice

import com.google.inject.AbstractModule

import org.gradle.jacobo.plugins.resolver.DependencyResolver
import org.gradle.jacobo.plugins.resolver.WsdlDependencyResolver

/**
 * Guice module binding interfaces to implementation classes.
 * Used to configure everything to run through Guice's {@code createInjector}
 */
class WsdlPluginModule extends AbstractModule {
  
  /**
   * Configures this Guice's module
   */
  @Override
  protected void configure() {
    bind(DependencyResolver.class).to(WsdlDependencyResolver.class)
  }
}