package com.jacobo.gradle.plugins.extension

import org.gradle.api.Project

/**
 * customizable fields passed into the wsimport ant task in #ParseWsdlTask
 * @author djmijares
 * Created: Tue Dec 04 09:01:34 EST 2012
 */
class WsImportExtension { 

    /**
     * destination directory for the generated java code output from wsimport task @see ParseWsdlTask
     */
  String sourceDestinationDirectory

    /**
     * Boolean argument for wsimport ant task in @see ParseWsdlTask
     * @see <a href="https://jax-ws.java.net/2.1.5/docs/wsimportant.html">wsimport ant task</a>
     */
  boolean verbose

    /**
     * Boolean argument for wsimport ant task in @see ParseWsdlTask
     */
  boolean keep

    /**
     * Boolean argument for wsimport ant task in @see ParseWsdlTask
     */
  boolean xnocompile

    /**
     * Boolean argument for wsimport ant task in @see ParseWsdlTask
     */
  boolean fork

    /**
     * Boolean argument for wsimport ant task in @see ParseWsdlTask
     */
  boolean xdebug

    /**
     * argument for wsimport ant task, defines the specification version to generate code against.
     * specifically, "2.0" will generate version 2.0 compliant code. Will default to 2.2 when Java 7 is in more use
     */
  String target

    /**
     * argument for wsimport ant task, this value is populated in the generated code from wsimport.
     */
  String wsdlLocation
}