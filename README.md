gradle-wsdl-plugin
==================
[![Build Status](https://drone.io/github.com/djmijares/gradle-wsdl-plugin/status.png)](https://drone.io/github.com/djmijares/gradle-wsdl-plugin/latest)

Gradle plugin that defines some conventions for web service Projects.  
Eases the manual configuration of web service project by: 

      * Hooking in ant tasks to parse the wsdl with wsimport
      * Defining a convention to generate a correct WSDL name for the project, by parsing the project-name itself
      * Automatically finding web Service dependencies in the WSDL and associated schema it imports/includes
      * Populating a WAR file with all dependent Web Service Files (WSDL, XSD).

# using the plugin #
```groovy
buildscript {
  repositories {
    ivy {
      url 'http://dl.bintray.com/content/djmijares/gradle-plugins'
    }
   }

  dependencies {
    classpath 'com.jacobo.gradle:gradle-wsdl-plugin:1.0'
   }
}

apply plugin: 'wsdl'
```

# setting up the jaxws configurations #
You *need* the jaxws configuration to run the `parseWsdl` task, but that is the only task that has an external dependency. Any version of jaxws that you care to use will work.  I prefer to stay with the latest releases.

```groovy
    dependencies { 
      jaxws 'com.sun.xml.ws:jaxws-tools:2.2.8-promoted-b131'
      jaxws 'com.sun.xml.ws:jaxws-rt:2.2.8-promoted-b131'
    }
```

# List of available tasks #
* `parseWsdl`
	- run wsimport ant task on the WSDL file name.
	- needs to be run manually **for now**
	- obviously if you have an updated WSDL, you need to `parseWsdl` before you package it up with a `war`
* `war`
	- automatically generates the war like a regular WAR would, but also auto populates the war with the default `wsdl` directory containing the wsdl the project uses and will auto populate with the correct `schema` directory and all imported schemas that the wsdl depends on.
	- can be run manually, but is already hooked into the `build` task automatically!

The war would look like this (see the *hello-world-episode-binding-ws* example)

      drwxr-xr-x         0  20-Jan-2013  21:19:26  META-INF/
      -rw-r--r--        25  20-Jan-2013  21:19:26  META-INF/MANIFEST.MF
      drwxr-xr-x         0  20-Jan-2013  21:19:26  WEB-INF/
      drwxr-xr-x         0  20-Jan-2013  21:19:26  WEB-INF/classes/
      drwxr-xr-x         0  19-Jan-2013  21:00:16  WEB-INF/classes/helloworld/
      drwxr-xr-x         0  19-Jan-2013  21:00:16  WEB-INF/classes/helloworld/sample/
      drwxr-xr-x         0  19-Jan-2013  21:00:16  WEB-INF/classes/helloworld/sample/ibm/
      drwxr-xr-x         0  19-Jan-2013  21:00:16  WEB-INF/classes/helloworld/sample/ibm/com/
      -rw-r--r--       993  19-Jan-2013  21:00:16  WEB-INF/classes/helloworld/sample/ibm/com/HelloWorld.class
      -rw-r--r--      2194  19-Jan-2013  21:00:16  WEB-INF/classes/helloworld/sample/ibm/com/HelloWorldService.class
      -rw-r--r--      1837  19-Jan-2013  21:00:16  WEB-INF/classes/helloworld/sample/ibm/com/ObjectFactory.class
      drwxr-xr-x         0  20-Jan-2013  21:19:26  WEB-INF/lib/
      -rw-r--r--      2674  19-Jan-2013  21:00:14  WEB-INF/lib/hello-world-schema-0.1.jar
      drwxr-xr-x         0  20-Jan-2013  21:19:26  WEB-INF/wsdl/
      -rw-r--r--      2049  19-Jan-2013  20:56:44  WEB-INF/wsdl/HelloWorldEpisodeBindingService.wsdl
      drwxr-xr-x         0  20-Jan-2013  21:19:26  WEB-INF/schema/
      drwxr-xr-x         0  20-Jan-2013  21:19:26  WEB-INF/schema/HelloWorld/
      -rw-r--r--       581  19-Jan-2013  20:56:44  WEB-INF/schema/HelloWorld/HelloWorld.xsd
      - ----------  --------  -----------  --------  -----------------------------------------------------------------

Where `wsdl` and `schema` folders are auto populated on the fly :)
 
# Where to put all your WSDLs and schemas for big (or not so big) web service projects? (The Problem of Schema Document Duplication) #
I have seen quite a lot of ways to parse documents and include them in the `war` file the correct way.  I have found that keeping things **DRY** in the easiest manner is to have two folders at the root of the repository.

## Resulting Folder Conventions I like to use ##
* rootDir
    * wsdl
    * schema
        * episodes
    	* bindings
	

With this folder layout, any subproject can know where the documents are with `project.rootDir`, and the wsdl and xsd imports/includes (which are usually relative paths) can be written and won't ever have to change.  

### Schema projects ###
With this `rootDir` convention, you can have 1...N schema projects, generating schema code with a jaxb plugin, populate episode files to `schema/episodes` and jar the classes up as libraries that you can re-use in services projects.  Look at the [examples folder](examples) for more examples of usage.

Different names can exist, but this Convention, that for now **can't** be modified, is that these **two** folders **must exist** at the **root** of the gradle project (`project.rootDir`)

Additionally, I also put XJC generated "episode" files in an "episodes" folder at rootDir/schema/episodes.  This can be used later by this plugins wsimport task.

# WSDL Naming Conventions #
For a WS project, there must be at least one WSDL that the project depends on.  The convention for this plugin is that the WSDL name must:

    * Begin with a capitalized letter
    * Use camel case (every word has their first letter capitalized)
    * End with "Service"
    * Have a .wsdl extension

i.e.
	`SomeFileIGotService.wsdl`

# Project Naming Conventions #
Given the WSDL naming conventions, the project name must also follow similar conventions for the plugin to be able to locate the
appropriate WSDL file name.

It is assumed that for a web-service, your project name will follow these conventions:

    * project name is all lower case
    * every word in project name has a '-' (hyphen) to divide the words
    * must end in '-ws' (signifying that this is a web-service project)

i.e.
	`some-particular-service-ws`

## tidying up the build ##
With this project naming convention, it is nicely localized in the build to apply this plugin to *only* these folders with

```groovy
subprojects { project ->
  if(project.name.endsWith("-ws")) { 
    apply plugin: 'wsdl'

    dependencies { 
      jaxws 'com.sun.xml.ws:jaxws-tools:2.2.8-promoted-b131'
      jaxws 'com.sun.xml.ws:jaxws-rt:2.2.8-promoted-b131'
    }
  }
}
```

# Plugin Conventions #

This plugin has three separate conventions with sensible defaults that a user can override if he so chooses

## Wsdl Plugin Convention defaults ##
There is are only two overridable defaults for the WSDL plugin

        * wsdlDirectory -- File object to the wsdl Directory
     	* nameRules -- a map of naming rules to convert in the project name (more on this later)

## wsimport Conventions ##
Several boolean sensible defaults are defined to be passed into the wsimport task

	* verbose
	* keep
	* xnocompile
	* fork
	* xdebug
 
And a few other String defaults
    
	* sourceDestionationDirectory
	* target
	* wsdlLocation

Read more about these [conventions](docs/wsimport.md)

## wsdl WAR conventions ##
String defaults

	* wsdlWarDir
	* schemaWarDir

File defaults

	* resolvedWebServiceDir
	* resolvedWsdlDir
	* resolvedSchemaDir
   
Default Conventions
----------
These are the current default conventions

```groovy
wsdl {
     wsdlDirectory = new File(project.rootDir, "wsdl")
     wsimport {
     	      sourceDestinationDirectory = "src/main/java"
	      episodeDirectory = new File(project.rootDir, "schema/episodes")
     }
     wsdlWar {
     	     wsdlWarDir = "wsdl"
	     schemaWarDir = "schema"
	     resolvedWebServiceDir = project.file(new File(project.buildDir, "web-service"))
	     resolvedWsdlDir = project.file(new File(project.wsdl.wsdlWar.resolvedWebServiceDir, "wsdl"))
	     resolvedSchemaDir = project.file(new File(project.wsdl.wsdlWar.resolvedWebServiceDir, "schema"))
     }
}
```
If you need to override some defaults, check out a few [possible examples](docs/override-examples.md)

# Got a really REALLY long WSDL name?? #
I have seen WSDL names that can be really long, like *REALLY* long. This becomes a problem with the project name and wsdl naming conventions.  Before you know it, with a WSDL name 

  `ProjectNameIsSoLongDataManagementService.wsdl`

your project name can will like 

     `project-name-is-so-long-data-management-ws`

This is just too long of a project name for me (the WSDL needs to be long if that is how it is named, but not the project name)!

A tidy feature this plugin has is something called a "nameRule". This allows you to specify a map of strings to transform in your project name to get the correct wsdl name.

Basically, you can configure the extension closure with 
```groovy
wsdl {
     nameRules = ["-dm" : "DataManagement", "-isl" : "IsSoLong"]
}
```
Now, your project name becomes 

`project-name-isl-dm-ws`

Which is pretty cryptic, yes, but it really reduces the length of your project name.  And usually, a project will dictate a Service naming convention like "DataManagement" or "TransactionProcessing" that you can put into a name rule.  I like this a lot! 

# Examples #
You can find some examples in the [examples folder](examples)

# Plugin Improvements #
If you think this plugin could be better, please fork it!

In my head, I see a few [possible improvements](docs/possible-improvements.md)

This plugin is stems from my own experiences working with web services and using many of them in big projects. [My philosophy on this](docs/web-service-personal-philosophy.md) is mos likely irrelevant here, but can really speed up parsing by binding files.
