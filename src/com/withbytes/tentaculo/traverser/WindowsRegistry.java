package com.withbytes.tentaculo.traverser;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * Took from http://stackoverflow.com/questions/62289/read-write-to-windows-registry-using-java
 * and modified to return a WindosRegistryKey
 * @author Oleg Ryaboy, based on work by Miguel Enriquez and modified by Javier Morales
 */
public class WindowsRegistry {

    /**
     * @param location path in the registry
     * @param key registry key
     * @return registry value or null if not found
     */
    public static WindowsRegistryKey readRegistry(String location, String key) throws Exception {
        // Run reg query, then read output with StreamReader (internal class)
        Process process = Runtime.getRuntime().exec("reg query "
                + '"' + location + "\" /v " + key);
        StreamReader reader = new StreamReader(process.getInputStream());
        reader.start();
        process.waitFor();
        reader.join();
        String output = reader.getResult();

        // Output has the following format:
        // \n<Version information>\n\n<key>\t<registry type>\t<value>
        output = output.trim();
        if (output.length()==0) {
            return null;
        }

        // Parse out the value
        String[] parsed = output.split("\n")[1].split("\u0020\u0020\u0020\u0020");
        
        WindowsRegistryType type = WindowsRegistryType.UNKNOWN;
        
        if (parsed[2].equals("REG_SZ")){
            type = WindowsRegistryType.REG_SZ;
        }
        
        return new WindowsRegistryKey(key, type, parsed[3]);
    }

    static class StreamReader extends Thread {

        private InputStream is;
        private StringWriter sw = new StringWriter();

        public StreamReader(InputStream is) {
            this.is = is;
        }

        @Override
        public void run() {
            try {
                int c;
                while ((c = is.read()) != -1) {
                    sw.write(c);
                }
            } catch (IOException e) {
            }
        }

        public String getResult() {
            return sw.toString();
        }
    }

    public static void main(String[] args) throws Exception {

        // Sample usage
        WindowsRegistryKey value = WindowsRegistry.readRegistry("HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\"
                + "Explorer\\Shell Folders", "History");
        System.out.println(value.getValue());

        value = WindowsRegistry.readRegistry("HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\"
                + "Explorer\\Shell Folders", "{4C5C32FF-BB9D-43B0-B5B4-2D72E54EAAA4}");
        System.out.println(value.getValue());
    }
}
