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
    public void testBeginBackup_String_Descriptor() throws TentaculoException {
        //Seting up mocks
        IPathTranslator translator = mock(IPathTranslator.class);
        ITraverser traverser = mock(ITraverser.class);
        Descriptor descriptor = mock(Descriptor.class);
        TentaculoException exception = mock(TentaculoException.class);
        TraverserFactory factory = mock(TraverserFactory.class);
        DescriptorReader reader = mock(DescriptorReader.class);
        
        ArrayList<String> paths = new ArrayList<String>();
        String successTargetPath = "TARGET_SUCCESS";
        String failureTargetPath = "TARGET_FAIL";
        String exceptionTargetPath = "TARGET_EXCEPTION";
        String mixedTargetPath = "TARGET_MIXED";
        String folderName = "FOLDER_NAME";
        paths.add("1");
        paths.add("2");
        paths.add("3");
        
        when(descriptor.getFolderName())
                .thenReturn(folderName);
        
        when(factory.getPathTranslator())
                .thenReturn(translator);        
        when(factory.getTraverser())
                .thenReturn(traverser);
        
        when(translator.getPathsForSystem(descriptor))
                .thenReturn(null)
                .thenReturn(new ArrayList<String>())
                .thenReturn(paths);
        
        when(traverser.isGameInstalled(descriptor, translator))
                .thenReturn(false)
                .thenReturn(true);

        when(traverser.backup(anyString(),eq(translator),eq(successTargetPath)))
                .thenReturn(true);
        when(traverser.backup(anyString(),eq(translator),eq(failureTargetPath)))
                .thenReturn(false);
        when(traverser.backup(anyString(),eq(translator),eq(mixedTargetPath)))
                .thenReturn(false)
                .thenReturn(true);
                
        Tentaculo instance = new Tentaculo(factory, reader);
        
        //Test game not installed       
        assertEquals(false, instance.beginBackup(successTargetPath, descriptor));
        //Test null paths array
        assertEquals(false, instance.beginBackup(successTargetPath, descriptor));
        //Test empty paths array
        assertEquals(false, instance.beginBackup(successTargetPath, descriptor));
        
        //Test all paths failed to copy
        assertEquals(false, instance.beginBackup(failureTargetPath, descriptor));
        
        //Test success with all paths copying successfully
        assertEquals(true, instance.beginBackup(successTargetPath, descriptor));
        //Test success with a path not copying succesfully
        assertEquals(true, instance.beginBackup(mixedTargetPath, descriptor));
        
        verify(traverser, times(7)).isGameInstalled(eq(descriptor), eq(translator));
        verify(translator, times(6)).getPathsForSystem(eq(descriptor)); 
        verify(traverser, times(3)).backup(anyString(), eq(translator), eq(successTargetPath));
        verify(traverser, times(3)).backup(anyString(), eq(translator), eq(mixedTargetPath));
        verify(traverser, times(3)).backup(anyString(), eq(translator), eq(failureTargetPath));
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
