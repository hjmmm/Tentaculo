package com.withbytes.tentaculo.traverser;

import com.withbytes.tentaculo.TentaculoException;

/**
 * Classes that implement this interface help to translate the keywords used in
 * paths to complete paths to be used by an ITraverser.
 * @author Javier Morales <moralesjm at gmail.com>
 */
public interface IPathTranslator {
    /**
     * Translates a path from a descriptor file into an useful path for an ITraverser
     * if there is any problem resolving the path, a TentaculoException is thrown
     * @param originalPath Original path according to the descriptor file format
     * @return Translated path
     * @throws TentaculoException If there are problems with the translation
     */
    String translatePath(String originalPath) throws TentaculoException;

    /**
     * Checks if the operative system and version is supported by the class
     * implementing the interface
     * @param osName
     * @return
     */
    boolean supportsOperativeSystem(String osName, String osVersion);
}
