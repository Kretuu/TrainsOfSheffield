package uk.ac.sheffield.com2008.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatcher {
    public static boolean matchRegex(String regex, String text) {
        if(text == null) return false;
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        return m.matches();
    }
}
