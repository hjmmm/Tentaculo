
package com.withbytes.tentaculo.traverser.windows;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Javier Morales <moralesjm at gmail.com>
 */
public class WindowsPathTranslatorTest {

    public WindowsPathTranslatorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
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
     *  [STEAMNAME]		The name of your Steam account
     *  [STEAMID]		A numerical identifier for your Steam account
     *  [STEAMPATH]		The default location where Steam is installed
     *  [STEAMAPPS] 		The default location for Steam Games
     *  [STEAMCLOUD]		Files that synchronize with the Steam Cloud
     *  [ANY_DIRECTORY]         Search in all directories for the appropriate one. Can NOT be used as the last portion of the path.
     * </pre>
     * The function checks if the program is running under Windows and throws an
     * exception if it is not.
     * @param originalPath
     * @return Full path with translated keywords
     * @throws TentaculoException If the system is not Windows or the
     *         translations cannot be completed.
     */


    /**
     * Test of translatePath method, of class WindowsPathTranslator.
     * Checks to see that all the paths are translated but doesn't check if the
     * translation is accurate according to the system, this task is the
     * resposability of the other test cases for this class.
     */
    @Test
    public void testTranslatePath() throws Exception {
        String originalPath = "[LOCAL]/[USERNAME]/[NON_EXISTANT]";
        WindowsPathTranslator instance = new WindowsPathTranslator();
        String result = instance.translatePath(originalPath);
        assertEquals(false, result.contains("[LOCAL]"));
        assertEquals(false, result.contains("[USERNAME]"));
        assertEquals(true, result.contains("[NON_EXISTANT]"));
    }
    
    /**
     * Test of supportsOperativeSystem method, of class WindowsPathTranslator.
     */
    @Test
    public void testSupportsOperativeSystem() {
        String osName;
        String osVersion;
        WindowsPathTranslator instance = new WindowsPathTranslator();
        osName = "Windows 2000";
        osVersion = "";
        assertEquals(true, instance.supportsOperativeSystem(osName, osVersion));
        osName = "Windows Vista";
        osVersion = "";
        assertEquals(true, instance.supportsOperativeSystem(osName, osVersion));
        osName = "Windows 7";
        osVersion = "";
        assertEquals(true, instance.supportsOperativeSystem(osName, osVersion));
        osName = "Linux";
        osVersion = "";
        assertEquals(false, instance.supportsOperativeSystem(osName, osVersion));
    }

}