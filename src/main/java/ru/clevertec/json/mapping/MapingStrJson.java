package ru.clevertec.json.mapping;

import ru.clevertec.json.exception.FormJsonException;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapingStrJson {
    private final String modeArray = "(\\[{%d}).+?(]{%d})";
    private final String modeChar = "(" +"([0-9]{1,5})|" + "(\"\\\\[uU][0-9a-fA-F]{4}\")|" + "(\"?null\"?)|" +
            "(\"\\\\[bfnrt\"]\")|" + "(\".\")" + ")";
    private final String modeNum = "(([-0-9.eE]+)|(null))";

    private final String modeStr = "\"([^\"]*(\"{2})?[^\"]*)*\"";

    private MapingStrJson() {
    }

    public static MapingStrJson getMapingStrJson() {
        return new MapingStrJson();
    }

    public String getArrStr(String s, int quotes) throws FormJsonException {
        if (s == null) throw new FormJsonException("null");
        if (quotes < 1) throw new FormJsonException("quotes < 1");
        String format = String.format(this.modeArray, quotes, quotes);
        Pattern pattern = Pattern.compile(format);
        return getVal(pattern, s);
    }

    public String getNum(String s) throws FormJsonException {
        if (s == null) throw new FormJsonException("null");
        Pattern pattern = Pattern.compile(this.modeNum);
        return getVal(pattern, s);
    }

    public String getObject(String str) {
        if ("null".equals(str))
            return "null";
        if (str == null || !str.startsWith("{")) throw new FormJsonException("Warning: " + str);
        final StringBuilder stringBuilder = new StringBuilder();
        int qwantQuotes = 0;
        for (char y : str.toCharArray()) { if (y == '{') qwantQuotes++;
            if (y == '}') qwantQuotes--;
            stringBuilder.append(y);
            if (qwantQuotes == 0)  break;
        }

        if (qwantQuotes != 0) throw new FormJsonException("Incorrect \"{\", \"}\" count");

        return stringBuilder.toString();
    }

    private String getVal(Pattern pattern, String s) throws FormJsonException {
        Matcher matcher = pattern.matcher(s);
        return matcher.results().map(MatchResult::group).findFirst()
                .orElseThrow(() -> new FormJsonException("Error: " + s));
    }

    public String getStringValue(String s) throws FormJsonException {
        if (s == null) throw new FormJsonException("null");
        Pattern pattern = Pattern.compile(this.modeStr);
        return getVal(pattern, s);
    }

    public String getChar(String s) throws FormJsonException {
        if (s == null) throw new FormJsonException("null");
        Pattern pattern = Pattern.compile(this.modeChar);
        return getVal(pattern, s);
    }

}
