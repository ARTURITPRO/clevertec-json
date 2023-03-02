package ru.clevertec.json.parse;

import ru.clevertec.json.exception.FormJsonException;
import ru.clevertec.json.exception.ParsJsonException;
import ru.clevertec.json.mapping.MapingStrJson;

import java.util.HashMap;
import java.util.Map;

public class ParseObj {
    private final MapingStrJson mapingStrJson;

    private ParseObj() {
        this.mapingStrJson = MapingStrJson.getMapingStrJson();
    }


    public static ParseObj getInstance() {
        return Owner.obj;
    }

    public Map<String, String> parseJson(String s) {
        if (s == null) throw new FormJsonException("null");
        if (!(s.startsWith("{") && s.endsWith("}"))) {
            throw new ParsJsonException("no obj");
        }

        s = s.substring(1, s.length() - 1);
        Map<String, String> map = new HashMap<>();

        String name;
        String val = null;

        while (!s.isEmpty()) {
            name = getName(s);
            s = s.substring(name.length() + 1);

            if (isNull(s)) {
                val = "null";
            }
            if (isNumber(s.charAt(0))) {
                val = getNumber(s);
            }else if (isString(s.charAt(0))) {
                val = getString(s);
            }else if (isArray(s.charAt(0))) {
                val = getArr(s);
            } else if (isObject(s.charAt(0))) {
                val = getObj(s);
            }

            int valueLength = val.length() + 1;
            if (s.length() <=valueLength) {
                s = "";
            }else {
                s = s.substring(val.length() + 1);
            }

            map.put(normalizeName(name), val);
        }
        return map;
    }

    private static final class Owner {
        private static final ParseObj obj = new ParseObj();
    }

    private boolean isNull(String json) {
        return json.startsWith("null");
    }

    private String getObj(String s) {
        return mapingStrJson.getObject(s);
    }

    private String getArr(String s) {
        long brackets = s.chars().takeWhile(c -> c == '[').count();
        return mapingStrJson.getArrStr(s, (int)brackets);
    }

    private String getString(String json) {
        String s;
        try {
            s = mapingStrJson.getStringValue(json);
        }catch (FormJsonException e) {
            s = mapingStrJson.getChar(json);
        }
        return s;
    }

    private String getNumber(String s) {
        return mapingStrJson.getNum(s);
    }

    private String normalizeName(String s) {
        return s.replaceAll("\"", "");
    }

    private String getName(String s) {
        if (!s.startsWith("\"")) throw new ParsJsonException("name is not");
        StringBuilder stringBuilder = new StringBuilder();
        int quotes = 0;

        for (char c : s.toCharArray()) {
            if (quotes == 2) {
                break;
            }

            stringBuilder.append(c);
            if (c == '"') quotes++;
        }
        return stringBuilder.toString();
    }

    private boolean isString(char ch) {
        return ch =='"';
    }

    private boolean isNumber(char ch) {
        return ch >='0' && ch <='9' || ch =='-';
    }

    private boolean isObject(char ch) {
        return ch == '{';
    }

    private boolean isArray(char ch) {
        return ch == '[';
    }
}
