package com.withbytes.tentaculo.traverser;

import java.util.ArrayList;
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
        PathTranslatorHelpers instance = new PathTranslatorHelpers();
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
        String keyword = "me_too";
        String value = "me too replaced";
        PathTranslatorHelpers instance = new PathTranslatorHelpers();
        assertEquals(
                "[LOCAL]/[SOMETHing ELSE]/me too replaced/[andme]/aaaa/me too replaced",
                instance.setKeywordValue(path1, keyword, value));
        assertEquals(
                path2,
                instance.setKeywordValue(path2, keyword, value));
        assertEquals(
                "me too replaced/[SOMETHing ELSE]/[me_not]/[andme]/aaaa/[me]",
                instance.setKeywordValue(path3, keyword, value));
    }
}
