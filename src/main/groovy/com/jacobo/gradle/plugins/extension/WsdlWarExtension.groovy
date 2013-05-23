package com.jacobo.gradle.plugins.extension

import org.gradle.api.Project

/**
 * Contains defaults for the @see WsdlWarTask
 * @author djmijares
 * Created: Tue Dec 04 09:01:34 EST 2012
 */
class WsdlWarExtension {
    /**
     *  WAR defaults
     */
  String wsdlWarDir

    /**
     *  WAR defaults
     */
  String schemaWarDir

    /**
     *  resolved Output Dir for web service War Task @see WsdlWarTask
     */
  File resolvedWebServiceDir

    /**
     *  resolved Output Dir for web service War Task @see WsdlWarTask
     */
  File resolvedWsdlDir

    /**
     *  resolved Output Dir for web service War Task @see WsdlWarTask
     */
  File resolvedSchemaDir
}