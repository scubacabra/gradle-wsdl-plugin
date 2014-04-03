package org.gradle.jacobo.plugins

import com.google.inject.Guice
import com.google.inject.Injector
import org.gradle.jacobo.schema.guice.DocSlurperModule
import org.gradle.jacobo.plugins.ant.AntWsImport
import org.gradle.jacobo.plugins.converter.ProjectToWsdlFileConverter
import org.gradle.jacobo.plugins.extension.WsImportExtension
import org.gradle.jacobo.plugins.extension.WsdlPluginExtension
import org.gradle.jacobo.plugins.guice.WsdlPluginModule
import org.gradle.jacobo.plugins.resolver.WsdlDependencyResolver
import org.gradle.jacobo.plugins.task.ConvertProjNameToWsdl
import org.gradle.jacobo.plugins.task.WsdlResolveDependencies
import org.gradle.jacobo.plugins.task.WsdlWar
import org.gradle.jacobo.plugins.task.WsdlWsImport
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.WarPlugin
import org.gradle.api.tasks.bundling.War

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
     Injector injector = Guice.createInjector([new WsdlPluginModule(), new DocSlurperModule()])
     configureWsdlExtension(project)
     configureWsdlConfiguration(project)
     def convertTask = configureConversionTask(project)
     def resolverTask = configureResolverTask(project, convertTask, injector)
     configureWarTask(project, resolverTask)
     configureWsImportTask(project, resolverTask)
   }

   private void configureWsdlExtension(final Project project) { 
     extension = project.extensions.create("wsdl", WsdlPluginExtension, project)
     extension.with { 
       wsdlFolder    = "wsdl"
       schemaFolder  = "schema"
       episodeFolder = "schema/episodes"
       nameRules     = [:]
       episodes      = []
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
     Task convert = project.tasks.create(CONVERSION_TASK_NAME,
					 ConvertProjNameToWsdl)
     convert.description = "convert the project name via convention to the projects wsdl file"
     convert.group = WSDL_PLUGIN_TASK_GROUP
     convert.conventionMapping.projectName	= { project.name }
     convert.conventionMapping.wsdlDirectory	= { project.file(new File(project.rootDir, project.wsdl.wsdlFolder)) }
     convert.conventionMapping.nameRules	= { project.wsdl.nameRules }
     convert.conventionMapping.converter	= { new ProjectToWsdlFileConverter() }
     return convert
   }

   private configureResolverTask(final Project project, final Task convertTask,
				 def injector) {
     Task resolver = project.tasks.create(RESOLVE_DEPENDENCIES_TASK_NAME,
					  WsdlResolveDependencies)
     resolver.description = "resolve all the wsdl dependencies for this project"
     resolver.group = WSDL_PLUGIN_TASK_GROUP
     resolver.dependsOn(convertTask)
     resolver.conventionMapping.wsdlFile = { project.wsdl.wsdlFile }
     resolver.conventionMapping.dependencyResolver = { injector.getInstance(WsdlDependencyResolver.class) }
     return resolver
   }

   private configureWsImportTask(final Project project, final Task resolverTask) {
     Task pwt = project.tasks.create(WSIMPORT_TASK_NAME, WsdlWsImport)
     pwt.dependsOn(resolverTask)
     pwt.description = "parse the wsdl with jaxws and wsimport"
     pwt.group = WSDL_PLUGIN_TASK_GROUP
     pwt.conventionMapping.wsdlFile	= { project.wsdl.wsdlFile }
     pwt.conventionMapping.wsdlDependencies  = { project.wsdl.wsdlDependencies }
     pwt.conventionMapping.destinationDirectory	= { project.file(project.wsdl.wsimport.sourceDestinationDirectory) }
     pwt.conventionMapping.episodeFiles	= {
       // empty filecollection if no episodes listed, so that
       // files.files == empty set
       project.wsdl.episodes.isEmpty() ? project.files() :
       project.fileTree(dir:new File(project.rootDir, project.wsdl.episodeFolder),
			include: project.wsdl.episodes)
     }
     pwt.conventionMapping.antExecutor	= { new AntWsImport() }
   }

   private void configureWarTask(final Project project, final Task resolverTask) {
     Task oldWar = project.tasks.getByName('war')
     Task wsdlWar = project.tasks.replace(WarPlugin.WAR_TASK_NAME, WsdlWar)
     wsdlWar.group = oldWar.group
     wsdlWar.description = oldWar.description + " Also bundles the xsd and wsdl files this service depends on"
     wsdlWar.dependsOn(resolverTask)
     wsdlWar.conventionMapping.wsdlDependencies  = { project.wsdl.wsdlDependencies }
     wsdlWar.conventionMapping.wsdlFolder = { project.file(new File(project.rootDir, project.wsdl.wsdlFolder)) }
     wsdlWar.conventionMapping.schemaFolder = { project.file(new File(project.rootDir, project.wsdl.schemaFolder)) }
   }
}
