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
    static List<GroupedWsdlWarFiles> resolveRelativePathsToWar(File rootDir, List<File> includeFiles) {
        def resolvedPaths = []
        includeFiles.each { resolveFile ->
            def relPath = resolveFile.parentFile.path - rootDir.path - "/"
            def resolved = resolvedPaths.find { it.into == relPath }
            if (resolved) {
                log.debug("found resolved object {} , into {}, from {}, resolved Files {} for this relative Path {} and resolved File {} already", resolved, resolved.from, resolved.into, resolved.resolvedFiles, relPath, resolveFile)
                resolved.resolvedFiles << resolveFile
            } else {
                log.debug("no resolved object {} for this relative Path {} and resolved file {} yet, adding a new one", resolved, relPath, resolveFile)
                resolvedPaths << new GroupedWsdlWarFiles(into: relPath, resolvedFiles: [resolveFile], from: resolveFile.parentFile.path)
            }
        }
        return resolvedPaths
    }

}
