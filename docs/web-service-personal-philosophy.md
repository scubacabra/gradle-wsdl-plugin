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
I developed another plugin for jaxb schema generation and automatic episode file generation on a per `targetNamespace` basis (among some other tidy features) [my jaxb plugin for gradle](https://github.com/djmijares/gradle-jaxb-namespace-dependency) that is a possibility for use.  There is also another [jaxb-plugin](https://github.com/stianh/gradle-jaxb-plugin) for consideration.

### wsimport artifacts magic low number ###
That number is **3**.  Yup, if you bind the episode files correctly and write the wsdl in such a way as well, you can have wsimport only generate the **3** classes it generates at a minimum.  

* Service Interface lass for the Endpoint Implementation
* Object Factory class 
* Client Service Helper class for clients to get Port access and proxy objects to invoke remotely or locally

see the [examples folder](examples), and specifically, the [episode bound hello world project source](examples/hello-world-dual-episode-binding-ws) to see the minimum number of classes and where they are generated, and the [associated wsdl](examples/wsdl/HelloWorldDualEpisodeBindingService.wsdl) to see how to generate the minimum number of classes.  

#### Why is this awesome??? ####
This is legit because you aren't going to be repeating yourself generating schemas over and over in different projects.  You could do everything *once* and the parsing is going to go much faster, because it is binding, not re-parsing.  
