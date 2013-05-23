# Overriding defaults #
You can override the defaults in the build file with

```groovy
wsdl {
     
     wsimport {

     }

     wsdlWar {

     }
}
```

## Changing default wsdl folder conventions ##
Default convention for `wsdlDirectory` is `${rootDir}/wsdl`, if this is not the case, and the folder is actually `WSDL`, you can change it.  Be to sure to change the other properties to reflect this change

```groovy
wsdl { 
 wsdlDirectory = file(new File(project.rootDir, "WSDL"))
 wsdlWar {
  wsdlWarDir = "WSDL"
  resolvedWsdlDir = project.file(new File(project.wsdl.resolvedWebServiceDir, "WSDL"))
 }  
}
```

## Changing Default schema War folder conventions ##
Default conventions for schema folder in this plugin is `schema` at `project.rootDir`, if this is different, say `XMLSchema` then you should change these properties to reflect that.

```groovy
wsdl {
     wsdlWar {
          schemaWarDir = "XMLSchema"
	  resolvedSchemaDir = project.file(new File(project.wsdl.resolvedWebServiceDir, "XMLSchema"))
     }
}
```
