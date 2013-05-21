package com.jacobo.gradle.plugins.model

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

/**
 *  Process the relative paths of all dependent files to go into the war, takes a list of wsdl dependencies (absolute paths) --
 *  @see WsdlDependencyResolver#absolutePathDependencies
 *
 */
class WsdlWarRelativePathResolver {

    private static final Logger log = Logging.getLogger(WsdlWarRelativePathResolver.class)

    String into, from
    List<File> resolvedFiles = []

    /**
     * Takes a root directory and a list of absolute file dependencies @see WsdlDependencyResolver#absolutePathDependencies
     * Compares them against the root Dir of the project, finds their relative path files, groups like relative paths together
     * This handles the 'into' and 'from' and the 'included' files to go into copied WAR diretory in the build folder*
     * @param rootDir root Directory of the project (multi-build or not)
     * @param includeFiles a list of absolute File paths that are to be included in the WAR artifact
     * @return List<WsdlWarRelativePathResolver>
     */
    static List<WsdlWarRelativePathResolver> resolveRelativePathsToWar(File rootDir, List<File> includeFiles) {
        def resolvedPaths = []
        includeFiles.each { resolveFile ->
            def relPath = resolveFile.parentFile.path - rootDir.path - "/"
            def resolved = resolvedPaths.find { it.into == relPath }
            if (resolved) {
                log.debug("found resolved object {} , into {}, from {}, resolved Files {} for this relative Path {} and resolved File {} already", resolved, resolved.from, resolved.into, resolved.resolvedFiles, relPath, resolveFile)
                resolved.resolvedFiles << resolveFile
            } else {
                log.debug("no resolved object {} for this relative Path {} and resolved file {} yet, adding a new one", resolved, relPath, resolveFile)
                resolvedPaths << new WsdlWarRelativePathResolver(into: relPath, resolvedFiles: [resolveFile], from: resolveFile.parentFile.path)
            }
        }
        return resolvedPaths
    }

    /**
     * String representation of this class
     * @return
     */
    public String toString() {
        def out = "from ${from}, into ${into} \n"
        resolvedFiles.each {
            out += "${it}:"
        }
        return out
    }
}