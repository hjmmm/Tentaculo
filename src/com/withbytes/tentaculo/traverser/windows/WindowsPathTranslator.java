package com.withbytes.tentaculo.traverser.windows;

import com.withbytes.tentaculo.TentaculoException;
import com.withbytes.tentaculo.traverser.IPathTranslator;

/**
 * Implements the IPathTranslator interface for MS Windows file system paths
 * @author Javier Morales <moralesjm at gmail.com>
 */
public class WindowsPathTranslator implements IPathTranslator {

    /**
     * Translates an input with any of the following keywords into a full path:
     * <pre>
     *  [TEMP]			Temporal files directory for the current user.
     *  [PROGRAMS_32]		Program files directory for 32bits programs.
     *  [PROGRAMS_64]		Program files directory for 64bits programs.
     *  [APPDATA] 		The default location for standard application settings
     *  [LOCAL] 		The default location for system-specific application settings
     *  [PUBLICDATA] 		The default location for cross-user application settings
     *  [PUBLIC] 		The default location for cross-user system settings
     *  [SAVEDGAMES] 		The Windows Vista default Saved Games folder
     *  [USER_HOME] 		Current user home directory.
     *  [DOCUMENTS] 		The default location for the "My Documents" shell folder/library for the current user
     *  [USERNAME]		The name of the Windows account you use on your computer
     *  [STEAMNAME]		The name of your Steam account
     *  [STEAMID]		A numerical identifier for your Steam account
     *  [STEAMPATH]		The default location where Steam is installed
     *  [STEAMAPPS] 		The default location for Steam Games
     *  [STEAMCLOUD]		Files that synchronize with the Steam Cloud
     *  [ANY_DIRECTORY]         Search in all directories for the appropriate one. Can NOT be used as the last portion of the path.
     * </pre>
     * The function checks if the program is running under a supported version of Windows and throws an
     * exception if it is not.
     * @param originalPath
     * @return Full path with translated keywords
     * @throws TentaculoException If the system is not Windows or the
     *         translations cannot be completed.
     */
    public String translatePath(String originalPath) throws TentaculoException {
        return "";
    }

    public boolean supportsOperativeSystem(String osName, String osVersion) {
        return osName.equals("Windows 2000")
                || osName.equals("Windows Vista")
                || osName.equals("Windows 7");
    }
}
