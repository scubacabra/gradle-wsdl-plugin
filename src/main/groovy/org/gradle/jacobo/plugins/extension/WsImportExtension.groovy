package org.gradle.jacobo.plugins.extension
/**
 * WsImport default ant task settings and conventions
 */
class WsImportExtension { 

  /**
   * Destination directory for the generated wsimport java output.
   * Path is relative to the projects directory, <b>not</b> the rootDirectory.
   * See <a href="https://jax-ws.java.net/2.1.5/docs/wsimportant.html">wsimport ant task</a>.
   */
  String sourceDestinationDirectory

  /**
   * Boolean argument for wsimport ant task.
   * See <a href="https://jax-ws.java.net/2.1.5/docs/wsimportant.html">wsimport ant task</a>.
   */
  boolean verbose

  /**
   * Boolean argument for wsimport ant task.
   * See <a href="https://jax-ws.java.net/2.1.5/docs/wsimportant.html">wsimport ant task</a>.
   */
  boolean keep

  /**
   * Boolean argument for wsimport ant task.
   * See <a href="https://jax-ws.java.net/2.1.5/docs/wsimportant.html">wsimport ant task</a>.
   */
  boolean xnocompile

  /**
   * Boolean argument for wsimport ant task.
   * See <a href="https://jax-ws.java.net/2.1.5/docs/wsimportant.html">wsimport ant task</a>.
   */
  boolean fork

  /**
   * Boolean argument for wsimport ant task.
   * See <a href="https://jax-ws.java.net/2.1.5/docs/wsimportant.html">wsimport ant task</a>.
   */
  boolean xdebug

  /**
   * Argument for wsimport ant task.
   * See <a href="https://jax-ws.java.net/2.1.5/docs/wsimportant.html">wsimport ant task</a>.
   */
  String target

  /**
   * Argument for wsimport ant task.
   * Defaults to "FILL_IN_BY_SERVER".
   * See <a href="https://jax-ws.java.net/2.1.5/docs/wsimportant.html">wsimport ant task</a>.
   */
  String wsdlLocation

    /**
     * The target package name for the generated classes.
     */
  String targetPackage
}