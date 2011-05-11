package com.withbytes.tentaculo.traverser;

import java.io.File;
import java.util.List;
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
public class PathTranslatorHelpersTest {

    public PathTranslatorHelpersTest() {
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
     * Test of getKeywords method, of class PathTranslatorHelpers.
     */
    @Test
    public void testGetKeywords() {
        String path = "[LOCAL]/[SOMETHing ELSE]/[me_too]/[andme]/aaaa";
        PathTranslatorHelpers instance = PathTranslatorHelpers.getInstance();
        String[] expResult = {"LOCAL", "SOMETHing ELSE", "me_too", "andme"};
        List<String> result = instance.getKeywords(path);
        assertArrayEquals(expResult, result.toArray());

        path = "aaaaa/bbbb gfg/bbbbbbfg/eritd";
        String[] emptyResult = {};
        result = instance.getKeywords(path);
        assertArrayEquals(emptyResult, result.toArray());
    }

    /**
     * Test of setKeywordValue method, of class PathTranslatorHelpers.
     */
    @Test
    public void testSetKeywordValue() {
        String path1 = "[LOCAL]/[SOMETHing ELSE]/[me_too]/[andme]/aaaa/[me_too]";
        String path2 = "[LOCAL]/[SOMETHing ELSE]/[me_not]/[andme]/aaaa/[me]";
        String path3 = "[me_too]/[SOMETHing ELSE]/[me_not]/[andme]/aaaa/[me]";
        String path4 = "[me_too]/[TEMP]/[SOMETHing ELSE]/[me_not]/[andme]/aaaa/[me]";
        String keyword = "me_too";
        String value = "me too replaced";
        String keywordTemp = "TEMP";
        String valueTemp = "C:\\Users\\Javier\\AppData\\Local\\Temp\\";
        PathTranslatorHelpers instance = PathTranslatorHelpers.getInstance();
        assertEquals(
                "[LOCAL]/[SOMETHing ELSE]/me too replaced/[andme]/aaaa/me too replaced",
                instance.setKeywordValue(path1, keyword, value));
        assertEquals(
                path2,
                instance.setKeywordValue(path2, keyword, value));
        assertEquals(
                "me too replaced/[SOMETHing ELSE]/[me_not]/[andme]/aaaa/[me]",
                instance.setKeywordValue(path3, keyword, value));
        assertEquals(
                "[me_too]/"+valueTemp+"/[SOMETHing ELSE]/[me_not]/[andme]/aaaa/[me]",
                instance.setKeywordValue(path4, keywordTemp, valueTemp));

    }

    /**
     * Test of ensureFinalSeparator method, of class PathTranslatorHelpers.
     */
    @Test
    public void testEnsureFinalSeparator() {
        PathTranslatorHelpers instance = PathTranslatorHelpers.getInstance();
        String directory = "AAAA";
        String anotherDirectory = "DIRECTORY";
        String expResult = "AAAA"+File.separator;
        
        assertEquals(expResult, instance.ensureFinalSeparator(directory));
        assertEquals(expResult, instance.ensureFinalSeparator(expResult));
        assertEquals(null, instance.ensureFinalSeparator(null));
        assertEquals(anotherDirectory+File.separator, instance.ensureFinalSeparator(anotherDirectory));
        
    }

}
