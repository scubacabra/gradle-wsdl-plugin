package com.jacobo.gradle.plugins.util

import com.jacobo.gradle.plugins.model.GroupedWsdlWarFiles

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

/**
 * User: djmijares
 * Date: 5/22/13
 * Time: 1:10 PM
 */
class WsdlWarFileGrouper {
    private static final Logger log = Logging.getLogger(WsdlWarFileGrouper.class)

    /**
     * Takes a root directory and a list of absolute file dependencies @see WsdlDependencyResolver#absolutePathDependencies
     * Compares them against the root Dir of the project, finds their relative path files, groups like relative paths together
     * This handles the 'into' and 'from' and the 'included' files to go into copied WAR diretory in the build folder*
     * @param rootDir root Directory of the project (multi-build or not)
     * @param includeFiles a list of absolute File paths that are to be included in the WAR artifact
     * @return List<GroupedWsdlWarFiles>
     */
    static List<GroupedWsdlWarFiles> groupFilesWithCommonParentDirs(List<File> includeFiles) {
        def resolvedPaths = []
        includeFiles.each { resolveFile ->
            def resolved = resolvedPaths.find { it.groupedFolder == resolveFile.parentFile }
            if (resolved) {
                log.debug("found resolved object {} , grouped Folder {}, groupedFiles {}", resolved, resolved.groupedFolder, resolved.groupedFiles)
                resolved.groupedFiles << resolveFile.name
            } else {
                log.debug("no resolved object {} for this folder {} and  file {} yet, adding a new one", resolved, resolveFile.parentFile, resolveFile.name)
                def grouping = new GroupedWsdlWarFiles(groupedFolder: resolveFile.parentFile)
		grouping.groupedFiles << resolveFile.name
		resolvedPaths << grouping
            }
        }
        return resolvedPaths
    }

}
