Conventions For Source Layout
=============================

Obviously after the code from the WSDL is generated, there needs to
be an implementation class written for the endpoint interface.

Except that jaxb just kind of throws them all in the same folder (from
what I remember).  It would be kinda nice to have the plugin
re-arrange the package structure for you.  To give something like this
after parsing.

* `default.package.name`
  * SomeServiceInterface.java
  * ObjectFactory.java
  * impl
    * SomeServiceImplementation.java
  * client
    * ClientServiceHelper.java

With the `SomeServiceImplementation` being a template filled in.

Maybe this could work.  Separating the client source file would be nice as well.

Client Jar Task
===============

Maybe the client source can be packaged up into a jar for clients to use.
The plugin could define something to do the equivalent of:

```groovy
jar {
  from sourceSet.main.output
  exclude('default/package/name/impl/*.class')
}
```

Automatic Episode Name Finding
==============================

I think this would couple conventions between this plugin and the
[jaxb plugin](https://github.com/djmijares/gradle-jaxb-namespace-dependency)
I wrote.

When parsing the wsdl with `wsimport` it would be possible to
resolve the wsdl dependencies, find the targetNamespace of all the
xsd's imported and convert the targetNamespace to an episode file name
the same way the other plugin does.  Then you would not have to know
which episode files to bind.  It would bind them all.

**assuming** that you generated them with the other plugin in the same
manner or else it wouldn't work quite well.

it could be configured like so:

```groovy
wsdl {
 automateEpisodeBinding = true
}
```
