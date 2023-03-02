package ru.clevertec.json.read;

import lombok.NoArgsConstructor;
import ru.clevertec.json.read.interfaces.ClassTakeAnAffidavit;
import ru.clevertec.json.read.interfaces.OutwardTakeAnAffidavit;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@NoArgsConstructor
public class CharTakeAnAffidavit implements ClassTakeAnAffidavit {
    private final Map<String, Character> characterMap = Map.of(
            "\\b", '\b', "\\t", '\t', "\\n", '\n', "\\f", '\f', "\\r", '\r', "\\\"", '"');

    public static CharTakeAnAffidavit getCharTakeAnAffidavit() {
        return new CharTakeAnAffidavit();
    }

    public char hexToInt(String s) {
        AtomicInteger atomicInteger = new AtomicInteger();
        return (char) new StringBuilder(s.toLowerCase()).reverse().chars()
                .map(c -> c == 'a' ? 10 : c == 'b' ? 11 : c == 'c' ? 12 : c == 'd' ? 13 : c == 'e' ? 14 : c == 'f' ? 15 : c - '0')
                .map(i -> (int) (Math.pow(16.0, 0.0 + atomicInteger.getAndIncrement()) * i)).sum();
    }
    @Override
    public <T> T getObject(String s, Class<T> o, OutwardTakeAnAffidavit outwardTakeAnAffidavit) {
        if ("null".equals(s) || s == null) return null;
        final Character output;

        final String s1 = s.length() == 1 ? s : s.substring(1, s.length() - 1);

        if (isVoid(s1))
            output = characterMap.get(s1);

        else if (s1.startsWith("\\") && s1.length() > 2)
            output = hexToInt(s1.substring(2));
        else if (s.matches("\\d{1,5}"))
            output = (char) Integer.parseInt(s);
        else output = s1.charAt(s1.length() - 1);

        return (T) output;
    }
    @Override
    public boolean canReadClass(Class<?> aClass) {
        return aClass == Character.class || aClass == char.class;
    }
    private boolean isVoid(String s) {
        return characterMap.containsKey(s);
    }
}
