# Objective
I was frustrated using web services and trying to package a war.  I would have to have all the knowledge of what wsdl I was using for project, and then follow the trail to see what xsd's and/or other wsdl's it projects WSDL depended on. **OR** if I was lazy I would just say get all xsds at the top level folder.

So I decided to use Groovy's `XmlSlurper`.  After all, wsdl and xsd files are just formatted xml. :)

The idea was to associate a project with a particular wsdl.  Then you parse the wsdl, get the locations of the dependencies and parse the dependencies, to get their dependencies -- and so on until there were no more files to parse and all the dependencies were resolved.

That just got the list of files a project depended on for wsdl's and xsds.  I still needed to organize them (the same way they are organized at the root folder, because then the WAR wouldn't really work with relative names),  and then I needed to package them.

Additionally, I thought a task to parse the Wsdl would be nice, especially since the wsdl would already be defined to resolve its dependencies.  This task allows episode files to hook into the ant task, reducing the generated output if you have episode files.

# Gradle Tasks -- TODO need to rename the files, Tasks suffix is redundant given the packaging
**All Tasks are located at `com.jacobo.gradle.plugins.tasks`**

* parseWsdlTask - `ParseWsdl`
* resolveProjectWsdlDependenciesTask - `ResolveWsdlDependencies`
* groupWsdlWarFileTask - `GroupWsdlWarFiles`
* copyWsdlFileTask - `CopyWsdlWarFiles`
* warTask - `WsdlWar`
   * overwrite of Gradle default War Task

## Parsing the WSDL -- TODO need to rename task variables properly
`ParseWsdl` parses the wsdl, pretty basic task:
* fills out ant task params
* loops over episode files from episode directory, if there are episode files to use.

see the class source for field comments.  Most are flags for the wsimport ant task -- you can read more about them here [at the jaxws java.net page](https://jax-ws.java.net/2.2/docs/wsimportant.html).

  
# Resolving WSDL dependencies
`ResolveWsdlDependencies` resolves the wsdl dependencies.  A little bit involved.

The  class method `resolveProjectDependencies` takes the starting WSDL file for your project, and parses it, getting all the dependencies in the `import` section. This can be more WSDL's and XSD's -- these are one in the same when you parse them, really, and henceforth will be referred to as a *document*.

## Possibilities
* Each document has a finite number of depencies
* Different documents can depend on the same file.

# Flow
1. `resolveProjectDependencies` <- projectWsdl
2. pop unresolved  Dependency Document -> slurp Document -> `resolveDocumentDependencies`
3. resolveDocumentDependencies -> get relative Dependencies -> resolve each dependency (this particular one resolved, or needs to be later) (`resolveDependency`)
4. go back to #2

# Slurping Documents

## Slurping XSD Documents
*ALL* xsd dependencies are found under the root element of the document
```xsd
<xsd>
<xsd:import schemaLocation="some_RELATIVE_location"/>
<xsd:include schemaLocation="some_RELATIVE_location_2"/>
<other stuff after>
</xsd>
```
The only important parts are the `schemaLocation`'s. :)

## Slurping WSDL Documents
Wsdl documents find their wsdl imports at

```wsdl

```

they find their xsd imports at

```wsdl

```

**XSD imports** are the same as for the xsd files themselves, but the parent container is not the root document, but deeper in the wsdl document.

So if you have a hierarchy of Slurper objects you would have the base class

# DocumentSlurper
* `documentFile`
* `slurpedDocument`
* `documentDependencies` -> `Set<File>`
* `resolveDocumentDependencies`
  * lets implementing class resolve its dependencies how it needs to
  * finishes up by calling `resolveRelativePathDependencies`
* `resolveRelativePathDependencies(List<Set<String>> relativeDependencies)`
  * resolves all the implementing classes slurped relative URL's into full path File objects, adds the Files to `documentDependencies`

and implementing class
# XsdSlurper
**extends** `DocumentSlurper`
* `xsdIncludes` -> `Set<String>`
* `xsdImports` -> `Set<String>`
* `slurpDependencies(elements, elementCollection)`
  * takes array of elements for includes/imports dependencies and get's their `schemaLocation` adds to the elementCollection

and the highest implementing class (inherits `XsdSlurper` methods and fields)
# WsdlSlurper
**extends** `XsdSlurper`
* `wsdlImports` -> `Set<String>`
* `slurpDependencies(elements, elementCollection)` (OVERRIDE)
  * get wsdl import dependencies here
  * delegate to super (`XsdSlurper`) for xsd dependencies

