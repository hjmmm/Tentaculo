
package com.withbytes.tentaculo.traverser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implements utility functions to help construct PathTranslators
 * @author Javier Morales <moralesjm at gmail.com>
 */
public class PathTranslatorHelpers {
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
        return path.replaceAll("\\["+keyword+"\\]", value);
    }
}
