package com.jacobo.gradle.plugins.model

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

/**
 *  Model class that holds the grouping of WSDL and XSD files that share the same parent directory.
 *  For example with
 *  <code>
 *      ~/path/to/root/wsdl/wsdl.wsdl
 *      ~/path/to/root/schema/one/one.xsd
 *      ~/path/to/root/schema/one/test.xsd
 *      ~/path/to/root/schema/two/two.xsd
 *  </code>
 *  You would have 3 instances of this class, and for the 'one' schema path, this class would contain
 *  <code>
 *      groupedFolder = ~/path/to/root/schema/one
 *      groupedFiles = [one.xsd, test.xsd]
 *  </code>
 */
class GroupedWsdlWarFiles implements Serializable {
    private static final Logger log = Logging.getLogger(GroupedWsdlWarFiles.class)

    private static final long serialVersionUID = -2785173570257175145L

    /**
     * Common folder for this set of files @see #groupedFiles
     */
    File groupedFolder

    /**
     * list of file names that share the same folder @see #groupedFolder
     */
    List<String> groupedFiles = []
}