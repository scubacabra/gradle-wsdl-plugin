package com.jacobo.gradle.plugins.util

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

/**
 * Utility class that operates on list elements passed in to it.
 * @author djmijares
 */
class ListUtil {
  
  private static final Logger log = Logging.getLogger(ListUtil.class)  

  /**
   * Utility function that checks if a @List has the value @input
   * @param list is the list
   * @param input is the input to check if it is already contained in the list
   */
  static boolean isAlreadyInList(List list, input) { 
    return list.contains(input)
  }

  /**
   * Adds an element to the list, if the element does not already contain that element
   * @param list is the list to add an element to
   * @param input is the element to add to the list
   */
  static addElementToList(List list, input) { 
    log.debug("trying to add element {} to list {}", input, list)
    if(!isAlreadyInList(list, input)) { 
      log.debug("Input {} is not in list,  adding to it", input)
      list << input
    }
  }
}