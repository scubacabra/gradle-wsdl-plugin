package com.jacobo.gradle.plugins

import spock.lang.Specification

/**
 * utility methods that most every test uses.
 */
class BaseSpecification extends Specification {

  def getFileFromResourcePath(def path) {
    return new File(this.getClass().getResource(path).toURI())
  }

  def fakeProjectDir(def dirName) {
    def resource = this.getClass().getResource("/").getPath()
    def fakeProjectDir = new File(resource, dirName)
    return fakeProjectDir
  }
}