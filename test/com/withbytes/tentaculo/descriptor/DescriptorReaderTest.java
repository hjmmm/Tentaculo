package com.withbytes.tentaculo.descriptor;

import com.withbytes.tentaculo.TentaculoException;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit test for the DescriptorReader class.
 * @author Javier Morales <moralesjm at gmail.com>
 */
public class DescriptorReaderTest {

    private String testResourcesPath;

    public DescriptorReaderTest() {
        testResourcesPath = "C:\\Users\\Javier\\Documents\\My Dropbox\\Proyectos\\Tentaculo\\test\\resources\\";
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

    @Test
    public void testReadDescriptor() throws FileNotFoundException, IOException {
        String separator = File.separator;
        DescriptorReader descReader = new DescriptorReader();
        String descriptorPath = this.testResourcesPath+"one"+separator+"1.yml";
        FileInputStream input = new FileInputStream(descriptorPath);
        Descriptor desc = descReader.readDescriptor(input);
        input.close();
        assertEquals("And Yet It Moves", desc.getFolderName());
        assertEquals("[APPDATA]\\Broken Rules\\And Yet It Moves Steam", desc.getWindowsPaths().get(0));
        assertEquals("[APPDATA]\\Broken Rules\\And Yet Moves", desc.getWindowsPaths().get(1));
        assertEquals("[APPDATA]\\Broken Rules\\And Yet It Moves Steam", desc.getWindowsPaths().get(2));
    }

    @Test
    public void testReadAllDescriptors() throws TentaculoException, FileNotFoundException {
        String separator = File.separator;
        ArrayList<Descriptor> result;
        DescriptorReader descReader = new DescriptorReader();
        String descriptorPath0 = this.testResourcesPath+"empty"+separator;
        String descriptorPath1 = this.testResourcesPath+"one"+separator;
        String descriptorPath2 = this.testResourcesPath+"two"+separator;
        String descriptorFile = this.testResourcesPath+"one"+separator+"1.yml";
        String bogusPath = this.testResourcesPath+"fake_path"+separator;
        
        // Test all files are loaded
        result = descReader.loadDescriptors(descriptorPath0);
        assertEquals(0,result.size());
        result = descReader.loadDescriptors(descriptorPath1);
        assertEquals(1,result.size());
        result = descReader.loadDescriptors(descriptorPath2);
        assertEquals(2,result.size());

        //Test only directories are taken as parameter
        try {
            result = descReader.loadDescriptors(descriptorFile);
            fail("An exception should have been raised becasue the path is a file instead of a directory.");
        }
        catch(TentaculoException ex) {}

        //Test an expected FileNotFoundException is thrown when the directory doesn't exists
        try {
            result = descReader.loadDescriptors(bogusPath);
            fail("An exception should have been raised becasue the path doesn't exist.");
        }
        catch(TentaculoException ex) {}
    }

}
