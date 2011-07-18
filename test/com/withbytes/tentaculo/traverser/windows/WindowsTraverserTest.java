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
package com.withbytes.tentaculo.traverser.windows;

import java.io.File;
import com.withbytes.tentaculo.TentaculoException;
import com.withbytes.tentaculo.TestsConfiguration;
import java.util.ArrayList;
import com.withbytes.tentaculo.descriptor.Descriptor;
import com.withbytes.tentaculo.traverser.IPathTranslator;
import com.withbytes.tentaculo.traverser.PathTranslatorHelpers;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author Javier Morales <moralesjm at gmail.com>
 */
public class WindowsTraverserTest {
    
    public WindowsTraverserTest() {
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
     * Test of isGameInstalled method, of class WindowsTraverser.
     */
    @Test
    public void testIsGameInstalled() throws TentaculoException {        
        String base = TestsConfiguration.RESOURCES_PATH;
        IPathTranslator translator = spy(new WindowsPathTranslator(PathTranslatorHelpers.getInstance()));
        ArrayList<String> empty = new ArrayList<String>();
        ArrayList<String> notInstalled = new ArrayList<String>();
        notInstalled.add(base+"one\\non_existant\\something.txt");
        notInstalled.add(base+"one\\non_existant2\\");
        notInstalled.add(base+"one\\non_existant2\\non_existant.info");
        notInstalled.add(null);
        ArrayList<String> installed = new ArrayList<String>();
        installed.add(base+"one\\non_existant\\something.txt");
        installed.add(base+"one\\non_existant2\\");
        installed.add(base+"one\\");
        installed.add(null);
        
        Descriptor descriptor = mock(Descriptor.class);
        when(descriptor.getWindowsPaths())
                .thenReturn(empty)
                .thenReturn(notInstalled)
                .thenReturn(installed);
        
        WindowsTraverser instance = new WindowsTraverser();
        //Empty paths array
        assertEquals(false, instance.isGameInstalled(descriptor, translator));
        //Non-existant paths
        assertEquals(false, instance.isGameInstalled(descriptor, translator));
        //One path exists 
        assertEquals(true, instance.isGameInstalled(descriptor, translator));
        
        verify(descriptor, times(3)).getWindowsPaths();
        verify(translator, times(6)).translatePath(anyString());
    }

    /**
     * Test of backup method, of class WindowsTraverser.
     */
    @Test
    public void testBackup() throws Exception {
        //Create folder to use as destination
        File destination = new File(TestsConfiguration.RESOURCES_PATH+"test");
        destination.delete();
        destination.mkdir();
        
        IPathTranslator translator = mock(WindowsPathTranslator.class);
        WindowsTraverser instance = new WindowsTraverser();
        File probe;
        
        //Test cases
        String sourceFile = TestsConfiguration.RESOURCES_PATH + "two\\2.yml";
        String sourceDirectory = TestsConfiguration.RESOURCES_PATH + "two";
        String sourceNonExistant = TestsConfiguration.RESOURCES_PATH + "falsepath";

        when(translator.translatePath(anyString()))
                .thenReturn(sourceFile)
                .thenReturn(sourceDirectory)
                .thenReturn(sourceNonExistant);  

        
        //Checking file copy
        assertEquals(true, instance.backup(sourceFile, translator, destination.getAbsolutePath()));
        probe = new File(destination.getAbsolutePath(),"2.yml");
        assertEquals(true, probe.exists());
        FileUtils.cleanDirectory(destination);
        
        //Checking directory copy
        assertEquals(true, instance.backup(sourceDirectory, translator, destination.getAbsolutePath()));
        probe = new File(destination.getAbsolutePath(),"two");
        assertEquals(true, probe.exists());
        probe = new File(destination.getAbsolutePath(),"two\\1.yml");
        assertEquals(true, probe.exists());
        FileUtils.cleanDirectory(destination);

        //Checking source doesn't exists
        assertEquals(false, instance.backup(sourceNonExistant, translator, destination.getAbsolutePath()));
        assertEquals(0, FileUtils.sizeOfDirectory(destination));
        
        //Verify mock
        verify(translator, times(3)).translatePath(anyString()); 
        
        //Remove existent destination folder and files
        FileUtils.deleteQuietly(destination);
    }

    /**
     * Test of restore method, of class WindowsTraverser.
     */
    @Test
    public void testRestore() throws Exception {
        String backupPath = "";
        IPathTranslator translator = null;
        String sourcePath = "";
        WindowsTraverser instance = new WindowsTraverser();
        // TODO Implement the restore test
        //instance.restore(backupPath, translator, sourcePath);        
    }
}
