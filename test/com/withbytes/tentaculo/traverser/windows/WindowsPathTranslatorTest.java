package com.withbytes.tentaculo.traverser.windows;

import com.withbytes.tentaculo.TentaculoException;
import java.util.prefs.BackingStoreException;
import org.powermock.core.classloader.annotations.*;
import org.powermock.modules.junit4.PowerMockRunner;
import org.junit.runner.RunWith;
import com.withbytes.tentaculo.TestsConfiguration;
import com.withbytes.tentaculo.traverser.PathTranslatorHelpers;
import com.withbytes.tentaculo.traverser.WindowsRegistryKey;
import com.withbytes.tentaculo.traverser.WindowsRegistryType;
import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

/**
 *
 * @author Javier Morales <moralesjm at gmail.com>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest( { WindowsPathTranslator.class })
public class WindowsPathTranslatorTest {

    private PathTranslatorHelpers defaultHelperMock;

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
        this.defaultHelperMock = spy(PathTranslatorHelpers.getInstance());
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of translatePath method, of class WindowsPathTranslator.
     * Checks to see that all the paths are translated but doesn't check if the
     * translation is accurate according to the system, this task is the
     * resposability of the other test cases for this class.
     */
    @Test
    public void testTranslatePath() throws Exception {
        String originalPath = "[TEMP]/[PROGRAMS_32]/[PROGRAMS_64]/[APPDATA]/[LOCAL]/[PUBLICDATA]/[PUBLIC]/[SAVEDGAMES]/[USER_HOME]/[DOCUMENTS]/[USERNAME]/[NON_EXISTANT]";
        
        WindowsPathTranslator instance = new WindowsPathTranslator(defaultHelperMock);
        String expected = instance.getTemporalDirectory()+"/"+
                instance.getPrograms32Directory()+"/"+
                instance.getPrograms64Directory()+"/"+
                instance.getAppDataDirectory()+"/"+
                instance.getLocalDirectory()+"/"+
                instance.getPublicDataDirectory()+"/"+
                instance.getPublicDirectory()+"/"+
                instance.getSavedGamesDirectory()+"/"+
                instance.getUserHome()+"/"+
                instance.getDocumentsDirectory()+"/"+
                instance.getUsername()+"/[NON_EXISTANT]";
        String result = instance.translatePath(originalPath);
        assertEquals(expected, result);
        
        verify(defaultHelperMock, times(11)).setKeywordValue(anyString(),anyString(), anyString());
    }

    /**
     * Test of translatePath method, of class WindowsPathTranslator
     * validating the behavior when the ANY_DIRECTORY keyword is used.
     */
    @Test
    public void testTranslatePathWithAnyDirectory() throws Exception {
        String originalPath = TestsConfiguration.RESOURCES_PATH + "[ANY_DIRECTORY]\\hint_directory\\";
        WindowsPathTranslator instance = new WindowsPathTranslator(defaultHelperMock);
        String expected = instance.processAnyDirectory(originalPath);
        String result = instance.translatePath(originalPath);
        assertEquals(expected, result);
    }

    
    /**
     * Test of supportsOperativeSystem method, of class WindowsPathTranslator.
     */
    @Test
    public void testSupportsOperativeSystem() {
        String osName;
        String osVersion;
        PathTranslatorHelpers mockedHelpers = mock(PathTranslatorHelpers.class);
        WindowsPathTranslator instance = new WindowsPathTranslator(mockedHelpers);
        osName = "Windows 2000";
        osVersion = "";
        assertEquals(false, instance.supportsOperativeSystem(osName, osVersion));
        osName = "Windows Vista";
        osVersion = "";
        assertEquals(true, instance.supportsOperativeSystem(osName, osVersion));
        osName = "Windows 7";
        osVersion = "";
        assertEquals(true, instance.supportsOperativeSystem(osName, osVersion));
        osName = "Linux";
        osVersion = "";
        assertEquals(false, instance.supportsOperativeSystem(osName, osVersion));
        verify(mockedHelpers, never()).getKeywords(anyString());
        verify(mockedHelpers, never()).setKeywordValue(anyString(),anyString(), anyString());
    }

    /**
     * Test of getTemporalDirectory method, of class WindowsPathTranslator.
     */
    @Test
    public void testGetTemporalDirectory() {
        WindowsPathTranslator instance = new WindowsPathTranslator(defaultHelperMock);
        String informedPath = "DIRECTORY";
        
        mockStatic(System.class);
        when(System.getenv("TEMP"))
                .thenReturn(informedPath)
                .thenReturn(informedPath+File.separator);
        
        String result = instance.getTemporalDirectory();
        assertEquals("Returns path with final separator even if the path informed by the system doesn't have it",
                informedPath+File.separator, result);
        result = instance.getTemporalDirectory();
        assertEquals("Returns the path with the final separator as recived from the system.", 
                informedPath+File.separator, result);
        
        verifyStatic(times(2));
        System.getenv("TEMP");
    }

    /**
     * Test of getPrograms64Directory method, of class WindowsPathTranslator.
     */
    @Test
    public void testGetPrograms64Directory() {
        WindowsPathTranslator instance = new WindowsPathTranslator(defaultHelperMock);
        String expected = "C:\\Program Files\\";
        
        mockStatic(System.class);
        when(System.getenv("ProgramFiles(x86)"))
                .thenReturn(null)
                .thenReturn("C:\\Program Files (x86)");
        when(System.getenv("ProgramFiles")).thenReturn("C:\\Program Files");
        
        //Should suppose the system is 32bits so null is returned
        assertEquals(null, instance.getPrograms64Directory());
        //Returns the program files folder as informed by the system
        assertEquals(expected, instance.getPrograms64Directory());

        verifyStatic(times(2));
        System.getenv("ProgramFiles(x86)");        
        verifyStatic();
        System.getenv("ProgramFiles");
    }

    /**
     * Test of getPrograms32Directory method, of class WindowsPathTranslator.
     */
    @Test
    public void testGetPrograms32Directory() {
        WindowsPathTranslator instance = new WindowsPathTranslator(defaultHelperMock);
        String expected32 = "C:\\Program Files\\";
        String expected64 = "C:\\Program Files (x86)\\";
        
        mockStatic(System.class);
        when(System.getenv("ProgramFiles(x86)"))
                .thenReturn(null)
                .thenReturn("C:\\Program Files (x86)");
        when(System.getenv("ProgramFiles")).thenReturn("C:\\Program Files");
        
        //Should suppose the system is 32bits so system path is returned
        assertEquals(expected32, instance.getPrograms32Directory());
        //Returns the (x86) program files folder as informed by the system
        assertEquals(expected64, instance.getPrograms32Directory());

        verifyStatic(times(2));
        System.getenv("ProgramFiles(x86)");
        verifyStatic();
        System.getenv("ProgramFiles");
    }

    /**
     * Test of getAppDataDirectory method, of class WindowsPathTranslator.
     */
    @Test
    public void testGetAppDataDirectory() {
        WindowsPathTranslator instance = new WindowsPathTranslator(defaultHelperMock);
        String expected = "C:\\Users\\Pepe\\AppData\\Roaming";
        
        mockStatic(System.class);
        when(System.getenv("APPDATA"))
                .thenReturn(null)
                .thenReturn(expected);
        
        assertEquals(null, instance.getAppDataDirectory());
        assertEquals(expected+File.separator, instance.getAppDataDirectory());

        verifyStatic(times(2));
        System.getenv("APPDATA");
    }

    /**
     * Test of getLocalDirectory method, of class WindowsPathTranslator.
     */
    @Test
    public void testGetLocalDirectory() {
        WindowsPathTranslator instance = new WindowsPathTranslator(defaultHelperMock);
        String expected = "C:\\Users\\Pepe\\AppData\\Local";
        
        mockStatic(System.class);
        when(System.getenv("LOCALAPPDATA"))
                .thenReturn(null)
                .thenReturn(expected);
        
        assertEquals(null, instance.getLocalDirectory());
        assertEquals(expected+File.separator, instance.getLocalDirectory());

        verifyStatic(times(2));
        System.getenv("LOCALAPPDATA");
    }

    /**
     * Test of getPublicDataDirectory method, of class WindowsPathTranslator.
     */
    @Test
    public void testGetPublicDataDirectory() {
        WindowsPathTranslator instance = new WindowsPathTranslator(defaultHelperMock);
        String expected = "C:\\ProgramData";
        
        mockStatic(System.class);
        when(System.getenv("ProgramData"))
                .thenReturn(null)
                .thenReturn(expected);
        
        assertEquals(null, instance.getPublicDataDirectory());
        assertEquals(expected+File.separator, instance.getPublicDataDirectory());

        verifyStatic(times(2));
        System.getenv("ProgramData");
    }

    /**
     * Test of getPublicDirectory method, of class WindowsPathTranslator.
     */
    @Test
    public void testGetPublicDirectory() {
        WindowsPathTranslator instance = new WindowsPathTranslator(defaultHelperMock);
        String expected = "F:\\Users\\Public";
        
        mockStatic(System.class);
        when(System.getenv("PUBLIC"))
                .thenReturn(null)
                .thenReturn(expected);
        
        assertEquals(null, instance.getPublicDirectory());
        assertEquals(expected+File.separator, instance.getPublicDirectory());

        verifyStatic(times(2));
        System.getenv("PUBLIC");
    }

    /**
     * Test of getSavedGamesDirectory method, of class WindowsPathTranslator.
     */
    @Test
    public void testGetSavedGamesDirectory() throws BackingStoreException, TentaculoException {
        when(this.defaultHelperMock.readWindowsRegistryKey(
                WindowsPathTranslator.REGISTRY_SHELL_FOLDERS, 
                WindowsPathTranslator.REGISTRYKEY_SAVED_GAMES))
                .thenReturn(new WindowsRegistryKey(WindowsPathTranslator.REGISTRYKEY_SAVED_GAMES, 
                            WindowsRegistryType.REG_SZ, "C:\\Something\\Saved Games")).
                thenReturn(new WindowsRegistryKey(WindowsPathTranslator.REGISTRYKEY_SAVED_GAMES, 
                            WindowsRegistryType.REG_SZ, "C:\\Something\\Saved Games\\"))
                .thenReturn(null);
        WindowsPathTranslator instance = new WindowsPathTranslator(defaultHelperMock);
        assertEquals("C:\\Something\\Saved Games\\", instance.getSavedGamesDirectory());
        assertEquals("C:\\Something\\Saved Games\\", instance.getSavedGamesDirectory());
        assertEquals(null, instance.getSavedGamesDirectory());
        
        verify(this.defaultHelperMock,times(3)).readWindowsRegistryKey(WindowsPathTranslator.REGISTRY_SHELL_FOLDERS, 
                WindowsPathTranslator.REGISTRYKEY_SAVED_GAMES);
    }

    /**
     * Test of getUserHome method, of class WindowsPathTranslator.
     */
    @Test
    public void testGetUserHome() {
        WindowsPathTranslator instance = new WindowsPathTranslator(defaultHelperMock);
        String expected = "F:\\Users\\UserName";
        
        mockStatic(System.class);
        when(System.getenv("USERPROFILE"))
                .thenReturn(null)
                .thenReturn(expected);
        
        assertEquals(null, instance.getUserHome());
        assertEquals(expected+File.separator, instance.getUserHome());

        verifyStatic(times(2));
        System.getenv("USERPROFILE");
    }

    /**
     * Test of getDocumentsDirectory method, of class WindowsPathTranslator.
     */
    @Test
    public void testGetDocumentsDirectory() throws TentaculoException {
        when(this.defaultHelperMock.readWindowsRegistryKey(
                WindowsPathTranslator.REGISTRY_SHELL_FOLDERS, 
                WindowsPathTranslator.REGISTRYKEY_PERSONAL))
                .thenReturn(new WindowsRegistryKey(WindowsPathTranslator.REGISTRYKEY_PERSONAL, 
                            WindowsRegistryType.REG_SZ, "C:\\Users\\Username\\Documents")).
                thenReturn(new WindowsRegistryKey(WindowsPathTranslator.REGISTRYKEY_PERSONAL, 
                            WindowsRegistryType.REG_SZ, "C:\\Users\\Username\\Documents\\"))
                .thenReturn(null);
        WindowsPathTranslator instance = new WindowsPathTranslator(defaultHelperMock);
        assertEquals("C:\\Users\\Username\\Documents\\", instance.getDocumentsDirectory());
        assertEquals("C:\\Users\\Username\\Documents\\", instance.getDocumentsDirectory());
        assertEquals(null, instance.getDocumentsDirectory());
        
        verify(this.defaultHelperMock,times(3)).readWindowsRegistryKey(WindowsPathTranslator.REGISTRY_SHELL_FOLDERS, 
                WindowsPathTranslator.REGISTRYKEY_PERSONAL);
    }

    /**
     * Test of getUsername method, of class WindowsPathTranslator.
     */
    @Test
    public void testGetUsername() {
        WindowsPathTranslator instance = new WindowsPathTranslator(defaultHelperMock);
        String expected = "Pepe";
        
        mockStatic(System.class);
        when(System.getenv("USERNAME"))
                .thenReturn(null)
                .thenReturn(expected);
        
        assertEquals(null, instance.getUsername());
        assertEquals(expected+File.separator, instance.getUsername());

        verifyStatic(times(2));
        System.getenv("USERNAME");
    }

    /**
     * Test of processAnyDirectory method, of class WindowsPathTranslator.
     */
    @Test
    public void testProcessAnyDirectory() throws TentaculoException {
        WindowsPathTranslator instance = new WindowsPathTranslator(defaultHelperMock);
        String pathSuccess = TestsConfiguration.RESOURCES_PATH + "[ANY_DIRECTORY]\\hint_directory";
        String pathUntouched = TestsConfiguration.RESOURCES_PATH + "random\\";
        String pathNotFound = TestsConfiguration.RESOURCES_PATH + "[ANY_DIRECTORY]\\false_directory";
        String pathExceptionDouble = TestsConfiguration.RESOURCES_PATH + "\\[ANY_DIRECTORY]\\[ANY_DIRECTORY]\\hint_directory";
        String pathExceptionOther = TestsConfiguration.RESOURCES_PATH + "\\[TEMP]\\[ANY_DIRECTORY]\\hint_directory";
        String pathExceptionLast = TestsConfiguration.RESOURCES_PATH + "random\\[ANY_DIRECTORY]";
        
        assertEquals(TestsConfiguration.RESOURCES_PATH + "random\\hint_directory\\",instance.processAnyDirectory(pathSuccess));
        assertEquals(pathUntouched,instance.processAnyDirectory(pathUntouched));
        assertEquals(null,instance.processAnyDirectory(pathNotFound));

        try {
            instance.processAnyDirectory(pathExceptionDouble);
            fail("The ANY_DIRECTORY keyword can only be used once in a path.");
        }
        catch(TentaculoException ex){}

        try {
            instance.processAnyDirectory(pathExceptionOther);
            fail("The ANY_DIRECTORY processing is not compatible with other keywords.");
        }
        catch(TentaculoException ex){}
        
        try {
            instance.processAnyDirectory(pathExceptionLast);
            fail("The ANY_DIRECTORY keyword can not be used as the last portion of the path.");
        }
        catch(TentaculoException ex){}
        
    }

}