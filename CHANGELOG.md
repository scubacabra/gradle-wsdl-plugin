# Gradle WSDL Plugin Changelog

## 1.5

### New Features

#### Name Rules
- Name Rules feature is added to turn abbreviations in the project name and substitute their meaning to find the appropriate WSDL name.  See the [readme](README.md)

#### Nested Extensions
Extensions broken up into `WsdlPluginExtension` and `WsImportExtension` to separate the wsimport defaults from cluttering up.  The [readme](README.md) shows how to configure.

### Tasks
- The plugin now has 5 distinct tasks to
  - Parse WSDL
    - task name: `parseWsdl`
    - using wsimport Ant Task
  - Get WSDL Name 
    - from the convention previously established project-name -> wsdl name
    - task name: `wsdlName`
  - Resolve WSDL dependencies 
    - find all file's required for WSDL project in WAR, 
    - task name : `resolveWSDLDependencies`
  - Group WSDL Files for WAR
    - Groups all the wsdl dependent files by the same parent directory and keeps track of the files in this directory that need to be included in the WAR file
    - task name: `groupWsdlWarFiles`
  - Copy WSDL Files
    - Copies all the grouped WSDL Files to the output directory `webServiceCopyDir`
    - task name: `copyWsdlWarFiles`

#### Overridden Task
- Gradle WAR task is overriden with added functionality to include the files in the output directory of the `copyWsdlWarFiles` task
  - see the `webServiceCopyDir` extension property

### Deployed to bintray
Bintray deploy used to be an ivy repository, now is deploying to a maven repository.

- url : `http://dl.bintray.com/content/djmijares/gradle-plugins/`
- group: `com.jacobo.gradle`
- package : `gradle-wsdl-plugin`
- version : `1.5`


### new script plugins
The build script was broken up into 

- `integTest.gradle`
- `cobertura.gradle`
- `buildscript.gradle`

for readability

#### Cobertura
Cobertura is added to this project for test coverage

### Complete Refactor
Alot of the code was refactored, I thought some of the methods did too much, and I broke them up and wrote test for them.

### Documentation
`docs` folder is holding all extra documentation for specific configuration and extra comments that are not pertinent to immediately getting set up with the default configuration.

### Testing

#### Integration Testing
- source set `integTest` in `src/main` has one integration test for this project.  Some of the unit tests might be moved here sometimes in the future, as they aren't true unit tests. 

### New Files
- `CopyWsdlWarFilesTask` and Spec `CopyWsdlWarFilesTaskSpec`
- `WsImportExtension`
- `WsdlSlurper` and Spec `WsdlSlurperSpec`
- `XsdSlurper` and Spec `XsdSlurperSpec`
- `DocumentReader` and Spec `DocumentReaderSpec`
- `FileHelper` and Spec `FileHelperSpec`
- `ListHelper` and Spec `ListHelperSpec`
- `WsdlWarFileGrouper` and Spec `WsdlWarFileGrouperSpec`
- `ResolveWsdlDependenciesTask`
- `GroupedWsdlWarFiles`
- `WsdlDependencyResolverIntegrationSpec`

### Deleted Files
- `WsdlWarRelativePathResolver`
- `WsdlResolverTask`

### Renamed Files
- `WsdlExtension` -> `WsdlPluginExtension`
- `WsdlName` -> `WsdlNameHelper`
- `WsdlNameSpec` -> `WsdlNameHelperSpec`
- `WsdlWarRelativePathResolverSpec` -> `GroupWsdlWarFilesTask`

