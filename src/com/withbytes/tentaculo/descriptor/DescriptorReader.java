package com.withbytes.tentaculo.descriptor;

import com.withbytes.tentaculo.TentaculoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import org.yaml.snakeyaml.Yaml;

/**
 * Reads a descriptor file and returns its representation as a POJO
 * @author Javier Morales <moralesjm at gmail.com>
 */
public class DescriptorReader {

    public Descriptor readDescriptor(InputStream descriptorStream) {
        HashMap descriptorHash;
        String folderName;
        ArrayList<String> windowsFiles = new ArrayList<String>();
        Yaml yaml = new Yaml();
        descriptorHash = (HashMap)yaml.load(descriptorStream);
        folderName = descriptorHash.get("Target folder name").toString();
        windowsFiles = (ArrayList<String>)descriptorHash.get("Windows source paths");
        return new Descriptor(folderName, windowsFiles);
    }

    ArrayList<Descriptor> loadDescriptors(String directoryToRead) throws TentaculoException, FileNotFoundException{
        ArrayList<Descriptor> result = new ArrayList<Descriptor>();
        File directory = new File(directoryToRead);
        File descriptors[];
        FileInputStream descriptorStream;
        if (!directory.isDirectory()){
            throw new TentaculoException("This method requires a directory to be passed as the path.");
        }
        descriptors = directory.listFiles();
        for (File descriptor : descriptors)
        {
            if (descriptor.isFile()){
                descriptorStream = new FileInputStream(descriptor);
                result.add(readDescriptor(descriptorStream));
            }
        }
        return result;
    }

}
