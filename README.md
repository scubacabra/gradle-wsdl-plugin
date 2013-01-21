gradle-wsdl-plugin
==================

plugin for gradle that sets up a wsdl task and sets up a convention between the sub project web service name and the wsdl document itself

# Project Name to Wsdl Name Conventions #

It is assumed that for a web-service, your project name will be of the form 

*some-particular-service-ws* where the *-ws* is **REQUIRED**

Here are some possible mappings of project name to wsdl service Straight from the tests

>  "spock-star-trek-ws"   >> "SpockStarTrekService" 

>  "srv-legend-ws"        >> "SrvLegendService" 
   
>  "boy-band-ws"          >> "BoyBandService" 

it takes the project name from a hyphenated value to camel case and replaces the  **-ws** with **Service**

# wsdl plugin conventions #
```groovy
wsdlDirectory -- File object to the wsdl Directory
wsdlFileName -- The wsdl Name found at the wsdl Directory
wsdlPath -- File object of the absolute path to the wsdl File
episodeDirectory  -- File object to the default episode File directory
```

*ASSUMING* you are generating schemas with the jaxb task (found in another plugin I developed [here](https://github.com/djmijares/gradle-jaxb-namespace-dependency) that you can use.  There is also another one found [here](https://github.com/stianh/gradle-jaxb-plugin)

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
to configure the wsimport task to bind with episode files at the **episodeDirectory** like so:
```groovy
wsdl {
  episodes = ["name-of-episode-file-sans-episode-extension"]
}
```
This will go and find the file **name-of-episode-file-sans-episode-extension.episode** at **episodeDirectory** even though you didn't include the episode file extension in the property configuration

# Examples #
You can find some examples in the *examples* directory

# List of available tasks #
* parseWsdl
    ** run wsimport ant task on the WSDL file name
* war
    ** automatically generates the war like a regular WAR would, but also auto populates the war with the default wsdl directory containing the wsdl the project uses and will auto populate with the correct schema directory and all imported schemas.  

The war would look like this (see the *hello-world-episode-binding-ws* example

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
