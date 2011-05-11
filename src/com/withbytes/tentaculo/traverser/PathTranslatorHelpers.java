
package com.withbytes.tentaculo.traverser;

import com.withbytes.tentaculo.TentaculoException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implements utility functions to help construct PathTranslators
 * @author Javier Morales <moralesjm at gmail.com>
 */
public class PathTranslatorHelpers {

    private static PathTranslatorHelpers instance = null;

    private PathTranslatorHelpers() {

    }

    public static PathTranslatorHelpers getInstance() {
        if (instance == null) {
            instance = new PathTranslatorHelpers();
        }
        return instance;
    }

    public List<String> getKeywords(String path) {
        ArrayList<String> result = new ArrayList<String>();
        Pattern pattern = Pattern.compile("\\[([\\w _]+)\\]");
        Matcher matcher = pattern.matcher(path);
        while (matcher.find()) {
            result.add(matcher.group(1));
        }
        return result;
    }
    
    public String setKeywordValue(String path, String keyword, String value) {
        value = Matcher.quoteReplacement(value);
        return path.replaceAll("\\["+keyword+"\\]", value);
    }
    
    public String ensureFinalSeparator(String directory) {
        if (directory!=null && !directory.endsWith(File.separator)){
            directory += File.separator;
        }
        return directory;
    }
    
    public WindowsRegistryKey readWindowsRegistryKey(String location, String key) throws TentaculoException {
        try {
            return WindowsRegistry.readRegistry(location, key);
        }
        catch(Exception ex){
            TentaculoException newExp = new TentaculoException("There was a problem while trying to read the registry key ["+key+"] in ["+location+"]", ex);
            throw newExp;            
        }
    }
}
