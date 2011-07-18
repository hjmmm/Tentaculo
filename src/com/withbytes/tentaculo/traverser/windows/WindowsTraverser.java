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

import com.withbytes.tentaculo.TentaculoException;
import com.withbytes.tentaculo.descriptor.Descriptor;
import com.withbytes.tentaculo.traverser.IPathTranslator;
import com.withbytes.tentaculo.traverser.ITraverser;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Javier Morales <moralesjm at gmail.com>
 */
public class WindowsTraverser implements ITraverser{

    public boolean isGameInstalled(Descriptor descriptor, IPathTranslator translator) throws TentaculoException{
        ArrayList<String> paths = descriptor.getWindowsPaths();
        File pathToCheck;
        for(String path : paths){ 
            if (path == null) { continue; }
            pathToCheck = new File(translator.translatePath(path));
            if (pathToCheck!=null && pathToCheck.exists()) {
                return true;
            }
        }
        return false;
    }

    public boolean backup(String sourcePath, IPathTranslator translator, String backupPath) throws TentaculoException {
        File source = new File(translator.translatePath(sourcePath));
        File destination = new File(backupPath);
        try {
            if (!source.exists()) {
                return false;
            }
            if (source.isFile()) {
                FileUtils.copyFileToDirectory(source, destination);
            }
            if (source.isDirectory()) {
                FileUtils.copyDirectoryToDirectory(source, destination);
            }
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    public boolean restore(String backupPath, IPathTranslator translator, String sourcePath) throws TentaculoException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
