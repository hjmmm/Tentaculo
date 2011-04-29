/*
 *  The MIT License
 *
 *  Copyright 2011 Javier Morales <moralesjm at gmail.com>.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package com.withbytes.tentaculo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.yaml.snakeyaml.Yaml;


/**
 * Main class for the Tentaculo application.
 * 
 * @author Javier Morales <moralesjm at gmail.com>
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Logger logger = Logger.getLogger(Main.class.getName());
            if (args.length != 1) {
                logger.log(Level.INFO, "Tentaculo expects the following parameters: \n\tPath to the configuration file");
                System.exit(1);
            }
            HashMap config = readConfiguration(args[0]);
            String descriptorsPath, targetPath;
            descriptorsPath = config.get("Game descriptors path").toString();
            targetPath = config.get("Target path").toString();
            logger.log(Level.INFO, "Descriptors path: {0}", descriptorsPath);
            logger.log(Level.INFO, "Target path: {0}", targetPath);
        } catch (Exception ex) {
            System.exit(1);
        }
    }

    /**
     * Reads the configuration file and returns a HashMap representation
     * @param configFilePath Path to the configuration file in the system
     * @return A HashMap with the keys for the file
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static HashMap readConfiguration(String configFilePath) throws FileNotFoundException, IOException {
        FileInputStream configFile = null;
        Object obj = null;
        try {
            Yaml yaml = new Yaml();
            configFile = new FileInputStream(configFilePath);
            obj = yaml.load(configFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "The configuration file was not found.", ex);
            throw ex;
        } finally {
            try {
                configFile.close();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Unexpected file error while reding the configuration file.", ex);
                throw ex;
            }
        }
        return (HashMap)obj;
    }

}
