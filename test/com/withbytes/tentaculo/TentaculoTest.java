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

import org.junit.Ignore;
import java.util.Map;
import java.io.File;
import com.withbytes.tentaculo.descriptor.Descriptor;
import com.withbytes.tentaculo.descriptor.DescriptorReader;
import com.withbytes.tentaculo.traverser.IPathTranslator;
import com.withbytes.tentaculo.traverser.ITraverser;
import com.withbytes.tentaculo.traverser.TraverserFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
public class TentaculoTest {

    ITraverser traverserMock;
    Descriptor descriptorMock;
    TentaculoException exceptionMock;
    TraverserFactory factoryMock;
    DescriptorReader readerMock;
    IPathTranslator translatorMock;
    String folderName = "FOLDER_NAME";
    
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
        translatorMock = mock(IPathTranslator.class);
        traverserMock = mock(ITraverser.class);
        descriptorMock = mock(Descriptor.class);
        exceptionMock = mock(TentaculoException.class);
        factoryMock = mock(TraverserFactory.class);
        readerMock = mock(DescriptorReader.class);
        
        when(descriptorMock.getFolderName())
                .thenReturn(folderName);        
        when(factoryMock.getPathTranslator())
                .thenReturn(translatorMock);
        when(factoryMock.getTraverser())
                .thenReturn(traverserMock);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of beginBackup method, of class Tentaculo. Success test.
     */
    @Test
    public void testBeginBackup_String_Descriptor_Success() throws TentaculoException {
        //Seting up mocks
        ArrayList<String> threePaths = new ArrayList<String>();
        threePaths.add("1");
        threePaths.add("2");
        threePaths.add("3");        
        ArrayList<String> onePath = new ArrayList<String>();
        onePath.add("1");
        ArrayList<String> twoNullsThreePaths = new ArrayList<String>();
        twoNullsThreePaths.add(null);
        twoNullsThreePaths.add(null);
        twoNullsThreePaths.add("3");        
        
        when(translatorMock.getPathsForSystem(descriptorMock))
                .thenReturn(onePath)
                .thenReturn(threePaths)
                .thenReturn(twoNullsThreePaths);
        
        when(traverserMock.isGameInstalled(descriptorMock, translatorMock))
                .thenReturn(true);

        when(traverserMock.backup(anyString(),eq(translatorMock), anyString()))
                .thenReturn(true);
                
        Tentaculo instance = new Tentaculo(factoryMock, readerMock);
        
        //Test game with 1 path
        assertEquals(true, instance.beginBackup("TARGET", descriptorMock));
        //Test game with 3 paths
        assertEquals(true, instance.beginBackup("TARGET", descriptorMock));
        //Test game with 3 paths being the first 2 null paths
        assertEquals(true, instance.beginBackup("TARGET", descriptorMock));

        verify(traverserMock, times(3)).isGameInstalled(eq(descriptorMock), eq(translatorMock));
        verify(translatorMock, times(3)).getPathsForSystem(eq(descriptorMock)); 
        verify(traverserMock, times(5)).backup(anyString(), eq(translatorMock), anyString());
    }

    /**
     * Test of beginBackup method, of class Tentaculo. Fail test.
     */
    @Test
    public void testBeginBackup_String_Descriptor_Fail() throws TentaculoException {
        fail("This test needs to be coded properly");
        //Check without paths
        //Check with all null paths
        //Check with game not installed
        //Check with exception
    }
        
    /**
     * Test of beginBackup method, of class Tentaculo.
     */
    @Test
    public void testBeginBackup_String_String() throws TentaculoException, FileNotFoundException, IOException {
        //Seting up mocks
        IPathTranslator translator = mock(IPathTranslator.class);
        ITraverser traverser = mock(ITraverser.class);
        Descriptor descriptor1 = mock(Descriptor.class);
        Descriptor descriptor2 = mock(Descriptor.class);
        TraverserFactory factory = mock(TraverserFactory.class);
        DescriptorReader reader = mock(DescriptorReader.class);
        TentaculoException exception = new TentaculoException("ERR");
        String targetPath = System.getProperty("java.io.tmpdir");
        Map<Descriptor,Boolean> result;
        
        File descriptorsPath = mock(File.class);
        File descriptorFile = mock(File.class);
        File[] descriptors = {descriptorFile,descriptorFile};
        File[] emptyFileList = {};
        
        when(factory.getPathTranslator())
                .thenReturn(translator);        
        when(factory.getTraverser())
                .thenReturn(traverser);
        
        when(descriptor1.getFolderName())
                .thenReturn("test1");
        when(descriptor2.getFolderName())
                .thenReturn("test2");
        
        when(descriptorsPath.listFiles())
                .thenReturn(emptyFileList)
                .thenReturn(descriptors);
        
        when(descriptorFile.getPath())
                .thenReturn(File.createTempFile("test", "file").getPath());
        
        when(reader.readDescriptor(any(InputStream.class)))
                .thenReturn(descriptor1, descriptor2, 
                            descriptor1, descriptor2,
                            descriptor1, descriptor2);
        
        Tentaculo instance = new Tentaculo(factory, reader);
        instance = spy(instance);
        doReturn(false).when(instance).beginBackup(anyString(), eq(descriptor1));
        doReturn(true).when(instance).beginBackup(anyString(), eq(descriptor2));
        
        //Test empty directory
        assertEquals(0,instance.beginBackup(targetPath, descriptorsPath).size());
        //Test 1 success
        result = instance.beginBackup(targetPath, descriptorsPath);
        assertEquals(2, result.size());
        assertEquals(false, result.get(descriptor1));
        assertEquals(true, result.get(descriptor2));
        //Test Exception
         try{
            doThrow(exception).when(instance).beginBackup(anyString(), eq(descriptor1));
            instance.beginBackup(targetPath, descriptorsPath);
            fail("An exception should have been raised.");
        }catch(TentaculoException ex){}
        //Test all success
        doReturn(true).when(instance).beginBackup(anyString(), eq(descriptor1));
        assertEquals(2,instance.beginBackup(targetPath, descriptorsPath).size());
        
        verify(descriptorsPath, times(4)).listFiles();
        verify(instance, times(5)).beginBackup(anyString(), any(Descriptor.class));
    }
}
