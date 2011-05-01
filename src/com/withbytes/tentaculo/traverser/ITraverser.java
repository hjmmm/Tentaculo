
package com.withbytes.tentaculo.traverser;

import com.withbytes.tentaculo.TentaculoException;

/**
 * Classes that implement this interface have to be able to take a path and
 * serialize it in another path.
 * The most direct implementation might involve traversing a directory tree
 * and copy all the files in it to the target directory; an implementation for
 * the windows registry might need to read registry keys and serialize them as
 * files.
 * @author Javier Morales <moralesjm at gmail.com>
 */
public interface ITraverser {
    /**
     * Serializes the information from the sourcePath into the packupPath
     * @param sourcePath Path where the information to backup can be found
     * @param translator Helper object to translate a path with keywords to
     *                   a full path
     * @param backupPath Target path for the backup, the serialized version of
     *                   the information will be store here
     * @throws TentaculoException If there are problems reading the source
     *         information or serializing it
     */
    void backupPath(String sourcePath, IPathTranslator translator, String backupPath) throws TentaculoException;
    /**
     * Takes the serialized version of the information found in the backupPath
     * and restores it to the sourcePath
     * @param backupPath Target path for the backup, the serialized version of
     *                   the information will be read from here
     * @param translator Helper object to translate a path with keywords to
     *                   a full path
     * @param sourcePath Path where the information will be restored
     * @throws TentaculoException If there are problems with the restoration of
     *                            the information of reading the backup
     */
    void restorePath(String backupPath, IPathTranslator translator, String sourcePath) throws TentaculoException;
}
