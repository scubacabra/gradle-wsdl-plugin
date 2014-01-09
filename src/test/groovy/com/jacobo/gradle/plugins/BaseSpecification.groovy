package com.jacobo.gradle.plugins

import spock.lang.Specification

class BaseSpecification extends Specification {
  def getFileFromResourcePath(path) {
    return new File(this.getClass().getResource(path).toURI())
  }
}