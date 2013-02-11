gradle-wsdl-plugin
==================

plugin for gradle that sets up a wsdl task and sets up a convention between the sub project web service name and the wsdl document itself

# using the plugin #
```groovy
buildscript {
 repositories {
  some-repo-where-this-is
  dependencies {
   classpath ':com.jacobo:gradle-wsdl:$version'
  } 
 }
}

apply plugin: 'wsdl'
```
# setting up the jaxws configurations #
You *need* the jaxws configuration to run the `parseWsdl` task, but that is the only task that has an external dependency. Any version of jaxws that you care to use will work.  I prefer to stay with the latest releases.

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

# Project Name to Wsdl Name Conventions #

It is assumed that for a web-service, your project name will be of the form 

*some-particular-service-ws* where the *-ws* is **REQUIRED**

Here are some possible mappings of project name to wsdl service Straight from the tests

    project-name           >>  wsdl-name
    "spock-star-trek-ws"   >> "SpockStarTrekService" 
    "srv-legend-ws"        >> "SrvLegendService" 
    "boy-band-ws"          >> "BoyBandService" 

it takes the project name from a hyphenated value to camel case and replaces the  **-ws** with **Service**

# wsdl plugin conventions #

    File wsdlDirectory -- File object to the wsdl Directory
    String wsdlFileName -- The wsdl Name found at the wsdl Directory
    File wsdlPath -- File object of the absolute path to the wsdl File
    File episodeDirectory  -- File object to the default episode File directory     
    //war defaults
    String wsdlWarDir -- String of the wsdl folder that will go into the war
    String schemaWarDir -- schema folder to go into war

    //resolved Output Dir
    File resolvedWebServiceDir -- resolved web service directory to go into project.buildDir
    File resolvedWsdlDir -- resolved wsdl dir to go into project.buildDir
    File resolvedSchemaDir -- resolved schema dir to go into project.buildDir

Default Conventions
----------
```groovy
wsdlDirectory = new File(project.rootDir, "wsdl")
sourceDestinationDirectory = "src/main/java"
episodeDirectory = new File(project.rootDir, "schema/episodes")
wsdlWarDir = "wsdl"
schemaWarDir = "schema"
resolvedWebServiceDir = project.file(new File(project.buildDir, "web-service"))
resolvedWsdlDir = project.file(new File(project.wsdl.resolvedWebServiceDir, "wsdl"))
resolvedSchemaDir = project.file(new File(project.wsdl.resolvedWebServiceDir, "schema"))
```

## Changing default wsdl folder conventions ##
Default convention for `wsdlDirectory` is `${rootDir}/wsdl`, if this is not the case, and the folder is actually `WSDL`, you can change it.  Be to sure to change the other properties to reflect this change

```groovy
wsdl { 
 wsdlDirectory = file(new File(project.rootDir, "WSDL"))
 wsdlWarDir = "WSDL"
 resolvedWsdlDir = project.file(new File(project.wsdl.resolvedWebServiceDir, "WSDL"))
}
```

## Changing Default schema War folder conventions ##
Default conventions for schema folder in this plugin is `schema` at `project.rootDir`, if this is different, say `XMLSchema` then you should change these properties to reflect that.

```groovy
schemaWarDir = "XMLSchema"
resolvedSchemaDir = project.file(new File(project.wsdl.resolvedWebServiceDir, "XMLSchema"))
```

## jaxws wsimport conventions ##

The plugin uses an ant jaxws wsimport task to parse the wsdl into java code, and the plugin is default configured with these variables and boolean values

```groovy
String sourceDestinationDirectory
boolean verbose = true
boolean keep = true
boolean xnocompile = true
boolean fork = false
boolean xdebug = false
String target = "2.1"
String wsdlLocation = "FILL_IN_BY_SERVER"
```

the **target** is defaulted to **2.1** because in my experience, not many people have updated to Java 7 yet, and I think it is easier to use something like 

```groovy
wsdl {
  target = "2.2"
}
```

**IF** you happen to be using java 7.  

configuration **sourceDestinationDirectory** is defaulted to **src/main/java** where your generated classes will normally go.  Some have different philosophies on if you should save your generated files to your repository.  I have always said that you should, so that if nothing has changed in your schema/wsdl documents, you don't have to run the tasks to parse those documents -- and that if something has changed, it will be reflected in your VC diff.

If you care to not keep your generated files, set *keep* to *false*.  See the available jaxws wsimport ant task options [here](http://jax-ws.java.net/2.2.3/docs/wsimportant.html)

## Binding to previously generated schema documents with jaxb episode binding ##
you can use the configuration 
    List episodes
to configure the wsimport task to bind with episode files at the **episodeDirectory** with

```groovy
wsdl {
  episodes = ["name-of-episode-file-sans-episode-extension"]
}
```

This will go and find the file **name-of-episode-file-sans-episode-extension.episode** at **episodeDirectory** even though you didn't include the episode file extension in the property configuration

# Examples #
You can find some examples in the [examples folder](examples)

# List of available tasks #
* `parseWsdl`
	- run wsimport ant task on the WSDL file name (determined from task `wsdlName`)
* `war`
	- automatically generates the war like a regular WAR would, but also auto populates the war with the default `wsdl` directory containing the wsdl the project uses and will auto populate with the correct `schema` directory and all imported schemas.  

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

# My Philosophy for web service projects #

## I like to keep things DRY ##
There are two areas where I have seen lots of repetition are

* Code **Re**Generation
* Schema Document **Duplication**

### Code Regeneration ###
This happens when the wsimport task has to re-parse the xsd schemas that it imports.  Usually though, there has already been a separate task to generate the code for those *same* xsd's because that code is shared across service projects.  

#### Episode Binding To The Rescue!!! ####
When you go through your xsd's and generate code for them, you can generate something called an *episode* file.  For more info, check out this java dot net [blog](http://weblogs.java.net/blog/kohsuke/archive/2006/09/separate_compil.html)

* * * * *
**Caveat**
* * * * *
Each `targetNamespace` in your xsd import collection needs to have it's own episode file.  So you can potentially bind with a bunch of episode files.  If you include a package name that isn't imported by your wsdl though, the jaxb parser that wsimport uses for xsd's will explode because it can't find the package to bind with.  

##### jaxb-plugin locations #####
I developed another plugin for jaxb schema generation and automatic episode file generation on a per `targetNamespace` basis (among some other tidy features) [here](https://github.com/djmijares/gradle-jaxb-namespace-dependency) that is a possibility for use.  There is also another jaxb-plugin found [here](https://github.com/stianh/gradle-jaxb-plugin).

### wsimport artifacts magic low number ###
That number is **3**.  Yup, if you bind the episode files correctly and write the wsdl in such a way as well, you can have wsimport only generate the **3** classes it generates at a minimum.  

* Service Interface lass for the Endpoint Implementation
* Object Factory class 
* Client Service Helper class for clients to get Port access and proxy objects to invoke remotely or locally

see the [examples folder](examples), and specifically, the [episode bound hello world project source](examples/hello-world-dual-episode-binding-ws) to see the minimum number of classes and where they are generated, and the [associated wsdl](examples/wsdl/HelloWorldDualEpisodeBindingService.wsdl) to see how to generate the minimum number of classes.  

#### Why is this awesome??? ####
This is legit because you aren't going to be repeating yourself generating schemas over and over in different projects.  You could do everything *once* and the parsing is going to go much faster, because it is binding, not re-parsing.  

# Schema Document Duplication #
I have seen quite a lot of ways to parse documents and include them in the `war` file the correct way.  I have found that keeping things DRY in the easiest manner is to have two folders at the root of the repository.

## Resulting Folder Conventions I like to use ##
* rootDir
    * wsdl
    * schema
        * episodes
    	* bindings
	

With this folder layout, any subproject can know where the documents are with `project.rootDir`, and the wsdl and xsd imports/includes (which are usually relative paths) can be written and won't ever have to change.  

### Schema projects ###
With this `rootDir` convention, you can have 1...N schema projects, generating schema code with a jaxb plugin, populate episode files to `schema/episodes` and jar the classes up as libraries that you can re-use in services projects.  Look at the [examples folder](examples) for more examples of usage.

# Plugin Improvements #
If you think this plugin could be better, fork it and send me a pull request. :)

In my head, I see a few [possible improvements](possible-improvements.md)

