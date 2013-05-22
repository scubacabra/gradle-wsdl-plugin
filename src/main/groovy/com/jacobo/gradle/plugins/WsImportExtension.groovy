package com.jacobo.gradle.plugins

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
  boolean verbose = true

    /**
     * Boolean argument for wsimport ant task in @see ParseWsdlTask
     */
  boolean keep = true

    /**
     * Boolean argument for wsimport ant task in @see ParseWsdlTask
     */
  boolean xnocompile = true

    /**
     * Boolean argument for wsimport ant task in @see ParseWsdlTask
     */
  boolean fork = false

    /**
     * Boolean argument for wsimport ant task in @see ParseWsdlTask
     */
  boolean xdebug = false

    /**
     * argument for wsimport ant task, defines the specification version to generate code against.
     * specifically, "2.0" will generate version 2.0 compliant code. Will default to 2.2 when Java 7 is in more use
     */
  String target = "2.1"

    /**
     * argument for wsimport ant task, this value is populated in the generated code from wsimport.
     */
  String wsdlLocation = "FILL_IN_BY_SERVER"

    /**
     * argument for wsimport ant task, contains the directory in the project structure where to find episode files
     */
  File episodeDirectory

    /**
     * argument for wsimport ant task, contains the specifc episodes to bind with, so that the wsimport task need not regenerate
     * already generated schema artifacts previously done with jaxb and xjc.
     */
  List episodes = []
}