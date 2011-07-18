package com.withbytes.tentaculo.traverser.windows;

import com.withbytes.tentaculo.TentaculoException;
import com.withbytes.tentaculo.descriptor.Descriptor;
import com.withbytes.tentaculo.traverser.IPathTranslator;
import com.withbytes.tentaculo.traverser.PathTranslatorHelpers;
import com.withbytes.tentaculo.traverser.WindowsRegistryKey;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the IPathTranslator interface for MS Windows file system paths
 * @author Javier Morales <moralesjm at gmail.com>
 */
public class WindowsPathTranslator implements IPathTranslator {
    public static final String REGISTRYKEY_SAVED_GAMES = "{4C5C32FF-BB9D-43B0-B5B4-2D72E54EAAA4}";
    public static final String REGISTRYKEY_PERSONAL = "Personal";
    public static final String REGISTRY_SHELL_FOLDERS = "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders";

    private PathTranslatorHelpers helper;

    public WindowsPathTranslator(PathTranslatorHelpers helper) {
        this.helper = helper;
    }

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
     *  [ANY_DIRECTORY]         Search in all directories for the appropriate one. Can NOT be used as the last portion of the path.
     * </pre>
     * The function checks if the program is running under a supported version of Windows and throws an
     * exception if it is not.
     * The parameters names and meanings are inspired by this post: 
     * http://forums.steampowered.com/forums/showthread.php?t=840657
     * @param originalPath
     * @return Full path with translated keywords
     * @throws TentaculoException If the system is not Windows or the
     *         translations cannot be completed.
     */
    public String translatePath(String originalPath) throws TentaculoException {
        if (originalPath==null) { return null; }
        List<String> keywords = this.helper.getKeywords(originalPath);
        String newPath = originalPath;
        for (String keyword:keywords){
            if (keyword.equals("TEMP"))
                newPath = helper.setKeywordValue(newPath, keyword, this.getTemporalDirectory());
            if (keyword.equals("PROGRAMS_32"))
                newPath = helper.setKeywordValue(newPath, keyword, this.getPrograms32Directory());
            if (keyword.equals("PROGRAMS_64"))
                newPath = helper.setKeywordValue(newPath, keyword, this.getPrograms64Directory());
            if (keyword.equals("APPDATA"))
                newPath = helper.setKeywordValue(newPath, keyword, this.getAppDataDirectory());
            if (keyword.equals("LOCAL"))
                newPath = helper.setKeywordValue(newPath, keyword, this.getLocalDirectory());
            if (keyword.equals("PUBLICDATA"))
                newPath = helper.setKeywordValue(newPath, keyword, this.getPublicDataDirectory());
            if (keyword.equals("PUBLIC"))
                newPath = helper.setKeywordValue(newPath, keyword, this.getPublicDirectory());
            if (keyword.equals("SAVEDGAMES"))
                newPath = helper.setKeywordValue(newPath, keyword, this.getSavedGamesDirectory());
            if (keyword.equals("USER_HOME"))
                newPath = helper.setKeywordValue(newPath, keyword, this.getUserHome());
            if (keyword.equals("DOCUMENTS"))
                newPath = helper.setKeywordValue(newPath, keyword, this.getDocumentsDirectory());
            if (keyword.equals("USERNAME"))
                newPath = helper.setKeywordValue(newPath, keyword, this.getUsername());
        }

        if (keywords.contains("ANY_DIRECTORY")) {
            newPath = this.processAnyDirectory(newPath);
        }

        return newPath;
    }

    public boolean supportsOperativeSystem(String osName, String osVersion) {
        return osName.equals("Windows Vista")
                || osName.equals("Windows 7");
    }

    private String getEnvironmentVariable(String name) {
        String value = System.getenv(name);
        return helper.ensureFinalSeparator(value);
    }
    
    private String getRegistryValue(String location, String key) throws TentaculoException {
        WindowsRegistryKey registryKey = this.helper.readWindowsRegistryKey(location, key);
        String path = null;
        if (registryKey!=null) {
            path = helper.ensureFinalSeparator(registryKey.getValue());
        }
        return path;
    }
    
    public String getTemporalDirectory() {
        return getEnvironmentVariable("TEMP");
    }

    public String getPrograms64Directory() {
        String program32 = System.getenv("ProgramFiles(x86)");
        String program64 = null;
        if (program32!=null) {
            program64 = System.getenv("ProgramFiles");
        }
        return helper.ensureFinalSeparator(program64);
    }

    public String getPrograms32Directory() {
        String program32 = System.getenv("ProgramFiles(x86)");
        if (program32==null) {
            program32 = System.getenv("ProgramFiles");
        }
        return helper.ensureFinalSeparator(program32);
    }

    public String getAppDataDirectory() {
        return getEnvironmentVariable("APPDATA");
    }

    public String getLocalDirectory() {
        return getEnvironmentVariable("LOCALAPPDATA");
    }

    public String getPublicDataDirectory() {
        return getEnvironmentVariable("ProgramData");
    }

    public String getPublicDirectory() {
        return getEnvironmentVariable("PUBLIC");
    }

    public String getSavedGamesDirectory() throws TentaculoException {
        return getRegistryValue(REGISTRY_SHELL_FOLDERS, REGISTRYKEY_SAVED_GAMES);
    }

    public String getUserHome() {
        return getEnvironmentVariable("USERPROFILE");
    }

    public String getDocumentsDirectory() throws TentaculoException {
        return getRegistryValue(REGISTRY_SHELL_FOLDERS, REGISTRYKEY_PERSONAL);
    }

    public String getUsername() {
        return getEnvironmentVariable("USERNAME");
    }

    public String processAnyDirectory(String path) throws TentaculoException{
        String[] portions = path.split("\\[ANY_DIRECTORY\\]\\\\");       
        File candidateFile;
        File baseFile = new File(portions[0]);        
        if (!validateAnyDirectoryUse(portions, path)) {
            return path;
        }        
        File[] candidates = baseFile.listFiles();        
        for(File candidate : candidates) {
            if (candidate.isDirectory()) {
                candidateFile = new File(candidate, portions[1]);
                if (candidateFile.exists()) {
                    return helper.ensureFinalSeparator(candidateFile.getAbsolutePath());
                }
            }
        }
        return null;
    }

    private boolean validateAnyDirectoryUse(String[] portions, String path) throws TentaculoException {
        if (portions.length!=2) {
            if (portions.length<2 && !path.contains("[ANY_DIRECTORY]")) {
                return false;
            } else {
                throw new TentaculoException("The [ANY_DIRECTORY] keyword can only be used once per path and is not allowed as the last element in the path.");
            }
        }
        if (helper.getKeywords(path).size()>1){
            throw new TentaculoException("The [ANY_DIRECTORY] keyword can not be used alongside other keywords.");
        }
        return true;
    }

    public ArrayList<String> getPathsForSystem(Descriptor descriptor) {
        return descriptor.getWindowsPaths();
    }
}
