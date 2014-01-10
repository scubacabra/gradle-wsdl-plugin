# Objective
I was frustrated using web services and trying to package a war.  I would have to have all the knowledge of what wsdl I was using for a project, and then follow the trail to see what xsd's and/or other wsdl's it projects WSDL depended on. **OR** if I was lazy I would just say get all xsds at the top level folder.  It was finally too tedious, *and I was using the lazy method, much to my chagrin, way too often for my liking*, that I decided to write a plugin for gradle to hopefully handle it.

Also, I really was tired of writing the same ant task to parse the wsdls using `wsimport` in gradle builds (and setting up the dependencies and all that good stuff to run the task) for every different project that was using wsdls.  The plugin **has** to define (all of) this for you.  And when you are parsing, if you have binding files for the xsd imports and you don't want those generated the plugin should allow you to specify an episode folder and binding names to bind with. Boom -- less generated source code. FTW! **^^**

## Steps to resolve the problem

1. To find the dependencies at `build` or `package` time, the documents need to be parsed for dependencies, and then dependencies of those dependencies, until you have all of the dependencies resolved. --> gives you some *set* of dependent `wsdl` and `xsd` related files for a project.**^^**

2. Got a set of dependencies, now need to package them in a war so they can be deployed without a thought to some App server (or whatever you use)**^^**

### Gotchas

1. Couldn't really find a way to go from set of files to packaging without an intermediate step.

       Had to copy the files from their original location (where they were parsed from) to a projects build directory -- using an ant copy task, requiring toDirectory, and files to put in that directory --  **in the same file structure** they originally were in.  Otherwise, the relative paths in the WSDL/XSD wouldn't be able to resolve if they weren't exact.**^^**

2. How do you know what project uses what WSDL.  You could write it down as an extension param, but that seems like a lot of work.  Or you could have a convention that gets the WSDL name from your project name. **^^**

3. Default war plugin wasn't going to cut it with the copying of files to build dir.  Needed to overwrite the functionality of the current War Task for this plugin, but keep the same name so that you don't have to type extra stuff to make your war.  `gradle wsdl-war` as opposed to `gradle war`... it should just be the latter. **^^**

## Tasks

All **^^** map to one of the tasks for this plugin.

**All Tasks are located in package** com.jacobo.gradle.plugins.tasks`

* parseWsdlTask - `ParseWsdl`
* resolveProjectWsdlDependenciesTask - `ResolveWsdlDependencies`
* groupWarFiles - `GroupWarFiles`
* copyWarFiles - `CopyWarFiles`
* warTask - `War`
   * overwrite of Gradle default War Task

### Task Flow
Only the parseWsdlTask stands alone.  It solely operates on configuration of extensions and defaults.

`ResolveWsdlDependencies` -> `groupWsdlWarFiles` -> `copyWsdlFiles` -> `war`
returns [] as Set *converted to an ArrayList* -> returns List<GroupedWsdlWarFiles> -> no return, copies to output directory, configured in extensions -> no return.

All returns are really just fields in the extensions that are set, because Tasks can't share return data.  But this is how they do it -- through the extensions.

# Some Decisions and thought processes
So I decided to use Groovy's `XmlSlurper`.  After all, wsdl and xsd files are just formatted xml!

The idea was to associate a project with a particular wsdl.  Then you parse the wsdl, get the locations of the dependencies and parse the dependencies -- to get the dependencies of those dependencies -- and so on until there were no more files to parse and all the dependencies were resolved. Woah, that's a lot of *dependencies* in a sentence.

That just got the set of files a project depended on for wsdl's and xsds.  The still need  to be organized (the same way they are organized at the root folder, because then the WAR wouldn't really work with relative names),  and then they need to be packaged up into a real `war` file.

##Convention over configuration
Some defaults are implemented but can be changed through the gradle plugin extensions.

# Task details (ins, out, returns, delegations etc.)

# Parsing the WSDL
`ParseWsdl` parses the wsdl, pretty basic task:
* fills out ant task params
    * see the class source for field comments.  Most are flags for the wsimport ant task -- you can read more about them here [at the jaxws java.net page](https://jax-ws.java.net/2.2/docs/wsimportant.html).
* loops over episode files from episode directory, if there are episode files to use.

Other Fields that aren't jaxb related
* episodeDirectory -> the directory where all the episode files are located
* episodes -> list of episode file names under the episode directory

delegates to the ant task created in the `ParseWsdl` Task object.
  
# Resolving WSDL dependencies
Task `ResolveWsdlDependencies` resolves the wsdl dependencies.

Input file is the projects WSDL File (taken care of through extensions as the user inputs dir to WSDL and WSDL name)

The task delegates actual processing to `WsdlDependencyResolver` and uses the return result from `resolveProjectDependencies` to populate the correct extension field.

## Processing
The  class method `resolveProjectDependencies` takes the starting WSDL file for your project, and parses it, getting all the dependencies in the `import` section. This can be more WSDL's and XSD's -- these are one in the same when you parse them, really, and henceforth will be referred to as a *document*.

### Possibilities
* Each document has a finite number of depencies
* Different documents can depend on the same file.

### Flow
1. `resolveProjectDependencies` <- projectWsdl
2. pop unresolved  Dependency Document -> slurp Document -> `resolveDocumentDependencies`
3. resolveDocumentDependencies -> get relative Dependencies -> resolve each dependency (this particular one resolved, or needs to be later) (`resolveDependency`)
4. go back to #2

## Slurping Documents
Slurp the document with Groovy's `XmlSlurper`

### Slurping XSD Documents
*ALL* xsd dependencies are found under the root element of the document
```xsd
<xsd>
<xsd:import schemaLocation="some_RELATIVE_location"/>
<xsd:include schemaLocation="some_RELATIVE_location_2"/>
<other stuff after>
</xsd>
```
The only important parts are the `schemaLocation`'s. :)

### Slurping WSDL Documents
Wsdl documents find their wsdl imports at

```wsdl

```

they find their xsd imports at

```wsdl

```

**XSD imports** are the same as for the xsd files themselves, but the parent container is not the root document, but deeper in the wsdl document.

## Slurper Objects
So if you have a hierarchy of Slurper objects you would have the base class:

### DocumentSlurper `DocumentSlurper.groovy`
* `documentFile`
* `slurpedDocument`
* `documentDependencies` -> `Set<File>`
* `resolveDocumentDependencies`
  * lets implementing class resolve its dependencies how it needs to
  * finishes up by calling `resolveRelativePathDependencies`
* `resolveRelativePathDependencies(List<Set<String>> relativeDependencies)`
  * resolves all the implementing classes slurped relative URL's into full path File objects, adds the Files to `documentDependencies`

and implementing class:
### XsdSlurper `XsdSlurper.groovy`
**extends** `DocumentSlurper`
* `xsdIncludes` -> `Set<String>`
* `xsdImports` -> `Set<String>`
* `slurpDependencies(elements, elementCollection)`
  * takes array of elements for includes/imports dependencies and get's their `schemaLocation` adds to the elementCollection

and the highest implementing class (inherits `XsdSlurper` methods and fields):
### WsdlSlurper `WsdlSlurper.groovy`
**extends** `XsdSlurper`
* `wsdlImports` -> `Set<String>`
* `slurpDependencies(elements, elementCollection)` (OVERRIDE)
  * get wsdl import dependencies here
  * delegate to super (`XsdSlurper`) for xsd dependencies

# Grouping WSDL Dependency Files Together
Task is used to group all wsdl dependent files that go in a war.  Returns a List of `GroupedWarFiles` objects.

Input -- `wsdlDependencies` List
Delegates to it's method `groupWarFilesByCommonDirectories`

## Grouped war files
Only contain
* groupedFolder File object
   The folder this file(s) are common to
* groupedFiles -- List of Strings
   The file names that go in the groupedFolder

Done this way because the ant task copy from asks for a directory and a list of names (as strings) of files that are in that directory that one would care to copy to the 'to' directory.

## Grouping
Just group by parentDirectory on the File objects.  If you already have a groupedFolder Object with the same parentFile then add it to that `GroupedWarFile` object. If not create a new and populate the correct fields.

# Copy War Files
Just loop over all the GroupedWarFiles objects that are in the Extensions, and for each put in the corect place in the webServicesOutDirectory
* find the relative difference of the groupedFolder to the projects rootDirectory.
   * Usually, the groupedFolders will be right under the Project Dir.  The project dir ("something/") and groupedFolder ("someting/wsdl").
   * then you would get the relative difference of "wsdl"
   * so you would put this "wsdl" folder at "webServicesOutDirectory" i.e. "webServicesOutDirectory/wsdl"
   * all the groupedFiles will be copied to here.

# War(ing) it up
Just points to the corect output directory from copy war wsdl, it should be two, the schema folder, and the wsdl folder, and it puts these files into the war file at the exact same spot.  So it basically mirrors any children of the fodler "webServicesOutDirectory"
