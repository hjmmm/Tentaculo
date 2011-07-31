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
import com.withbytes.tentaculo.descriptor.DescriptorReader;
import com.withbytes.tentaculo.traverser.IPathTranslator;
import com.withbytes.tentaculo.traverser.ITraverser;
import com.withbytes.tentaculo.traverser.TraverserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Javier Morales <moralesjm at gmail.com>
 */
public class Tentaculo {
    
    private TraverserFactory traverserFactory;
    private DescriptorReader descriptorReader;
    
    public Tentaculo(TraverserFactory traverserFactory, DescriptorReader descriptorReader){
        this.traverserFactory = traverserFactory;
        this.descriptorReader = descriptorReader;
    }
    
    public boolean beginBackup(String targetPath, Descriptor descriptor) throws TentaculoException{
        ITraverser traverser = traverserFactory.getTraverser();
        IPathTranslator translator = traverserFactory.getPathTranslator();
        ArrayList<String> paths;
        boolean success = false;
        String path;
        File targetPathFile = new File(targetPath, descriptor.getFolderName());
        targetPath = targetPathFile.getAbsolutePath();
        if (!traverser.isGameInstalled(descriptor, translator)){
            return false;
        }
        paths = translator.getPathsForSystem(descriptor);
        if (paths == null) {
            return false;
        }
        for (int i=0;i<paths.size(); i++){
            path = paths.get(i);
            targetPathFile = new File(targetPath, Integer.toString(i+1));
            if (path==null) { continue; }
            success = traverser.backup(path, translator, targetPathFile.getAbsolutePath()) || success;
        }
        return success;
    }

    public Map<Descriptor,Boolean> beginBackup(String targetPath, File descriptorsPath) throws TentaculoException{
        Map<Descriptor,Boolean> result = new HashMap<Descriptor, Boolean>();
        File targetDirectoryForDescriptor;
        ArrayList<Descriptor> descriptors = this.getDescriptors(descriptorsPath);
        for(Descriptor descriptor:descriptors){
            targetDirectoryForDescriptor = new File(targetPath, descriptor.getFolderName());
            targetDirectoryForDescriptor.mkdir();
            result.put(descriptor, 
                    this.beginBackup(targetDirectoryForDescriptor.getAbsolutePath(), descriptor));
        }
        return result;
    }
    
    public ArrayList<String> getTranslatedPaths(Descriptor descriptor) throws TentaculoException{
        IPathTranslator translator = traverserFactory.getPathTranslator();
        ArrayList<String> paths = translator.getPathsForSystem(descriptor);
        String translated;
        for(int i=0; i<paths.size(); i++){
            translated = translator.translatePath(paths.get(i));
            paths.set(i, translated);
        }
        return translator.getPathsForSystem(descriptor);
    }
    
    public ArrayList<Descriptor> getDescriptors(File descriptorsPath) throws TentaculoException {
        ArrayList<Descriptor> descriptors = new ArrayList<Descriptor>();
        File[] descriptorsFiles = descriptorsPath.listFiles();
        Descriptor descriptor;
        if (descriptorsFiles == null){
            throw new TentaculoException("There are no descriptors in the descriptors path or the system can't access them.");
        }
        for(File descriptorFile:descriptorsFiles){
            try{
                descriptor = this.descriptorReader.readDescriptor(new FileInputStream(descriptorFile));
                descriptors.add(descriptor);
            }catch(IOException ex){
                throw new TentaculoException(
                        "There was a problem while opening the descriptor at " + 
                        descriptorFile.getAbsolutePath(), ex);
            }
        }
        return descriptors;
    }
}
