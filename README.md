gradle-wsdl-plugin
==================

plugin for gradle that sets up a wsdl task and sets up a convention between the sub project web service name and the wsdl document itself

# Project Name to Wsdl Name Conventions #

It is assumed that for a web-service, your project name will be of the form 

*some-particular-service-ws* where the *-ws* is **REQUIRED**

Here are some possible mappings of project name to wsdl service Straight from the tests

>   projectName            | wsdl-name

>  "spock-star-trek-ws"   | "SpockStarTrekService" 

>  "srv-legend-ws"        | "SrvLegendService" 

>  "boy-band-ws"          | "BoyBandService" 

it takes the project name from a hyphenated value to camel case and replaces the  **-ws** with **Service**

# wsdl plugin conventions #

  wsdlDirectory -- File object to the wsdl Directory
  wsdlFileName -- The wsdl Name found at the wsdl Directory
  wsdlPath -- File object of the absolute path to the wsdl File
  episodeDirectory  -- File object to the default episode File directory (assuming you are generating schemas with the jaxb task (found in another plugin [here](https://github.com/djmijares/gradle-jaxb-namespace-dependency) that you can use

## jaxws wsimport conventions ##

The plugin uses an ant jaxws wsimport task to parse the wsdl into java code, and the plugin is default configured with these variables and boolean values

  String sourceDestinationDirectory
  boolean verbose = true
  boolean keep = true
  boolean xnocompile = true
  boolean fork = false
  boolean xdebug = false
  String target = "2.1"
  String wsdlLocation = "FILL_IN_BY_SERVER"

the **target** is defaulted to **2.1** because in my experience, not many people have updated to Java 7 yet, and I think it is easier to use something like 

    wsdl {
      	 target = "2.2"
    }

**IF** you happen to be using java 7.  

*sourceDestinationDirectory* is defaulted to *src/main/java* where your generated classes will normally go.  Some have different philosophies on if you should save your generated files to your repository.  I have always said that you should, so that if nothing has changed in your schema/wsdl documents, you don't have to run the tasks to parse those documents.  

If you care to not keep your generated files, set *keep* to *false*.  See the available jaxws wsimport ant task options [here](http://jax-ws.java.net/2.2.3/docs/wsimportant.html)

