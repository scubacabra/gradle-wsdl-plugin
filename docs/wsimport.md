# jaxws wsimport details #

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
