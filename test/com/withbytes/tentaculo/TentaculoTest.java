/*
 * The MIT License
 *
 * Copyright 2011 Javier Morales <moralesjm at gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.withbytes.tentaculo;

import com.withbytes.tentaculo.descriptor.Descriptor;
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
public class TentaculoTest {
    
    public TentaculoTest() {
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
     * Test of beginBackup method, of class Tentaculo.
     */
    @Test
    public void testBeginBackup_String_Descriptor() {
        String targetPath;
        String[] paths = {};
        Descriptor descriptor = null;
        Tentaculo instance = new Tentaculo();
        instance.beginBackup(targetPath, descriptor);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of beginBackup method, of class Tentaculo.
     */
    @Test
    public void testBeginBackup_String_String() {
        System.out.println("beginBackup");
        String targetPath = "";
        String descriptorsPath = "";
        Tentaculo instance = new Tentaculo();
        instance.beginBackup(targetPath, descriptorsPath);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
