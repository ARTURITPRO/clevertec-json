package ru.clevertec.json.write;

import ru.clevertec.json.exception.ParsJsonException;
import ru.clevertec.json.util.Util;
import ru.clevertec.json.write.interfaces.WriteJson;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class WriteJsonImpl implements WriteJson {

    @Override
    public String writeObjectAsString(Object obj) {
        if (obj == null) return "null";
        final Class<?> objClass = obj.getClass();
        final String json;

        if (Util.isNumber(obj)) json  = String.valueOf(obj);
        else if (objClass == Character.class) json = characterToString((char)obj);
        else if (objClass == String.class) json = "\"" + obj + "\"";
        else if (objClass.isArray()) json = arrayToString((Object[]) obj);
        else if (Util.isClassInstanceOf(objClass, Collection.class)) json = arrayToString(((Collection<?>) obj).toArray());
        else if (Util.isClassInstanceOf(objClass, Map.class)) json = mapToString((Map<?, ?>) obj);
        else json = "{" +
                    Util.getAllDeclaredFields(objClass).stream()
                            .filter(f -> !Modifier.isStatic(f.getModifiers()))
                            .collect(Collectors.toMap(
                                    field -> "\"" + field.getName() + "\"",
                                    field -> {
                                        field.setAccessible(true);
                                        Object fieldValue = getFieldValue(field, obj);
                                        return writeObjectAsString(fieldValue);
                                    }))
                            .entrySet().stream()
                            .map(entry -> entry.getKey() + ":" + entry.getValue())
                            .collect(Collectors.joining(","))
                    + "}";
        return json;
    }

    private Object getFieldValue(Field field, Object obj) {
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw new ParsJsonException(e);
        }
    }

    private String characterToString(char o) {
        if (o == 34) return "\"\\" + o + "\"";
        return o > 32 && o != 92 ? "\"" + o + "\"" : "" + (int)o;
    }

    private String arrayToString(Object[] o) {
        return  "[" +
                Arrays.stream(o)
                        .map(this::writeObjectAsString)
                        .collect(Collectors.joining(","))
                + "]";
    }
    private String mapToString(Map<?, ?> map) {
        return "{" +
                map.entrySet().stream()
                        .map(entry -> {
                            final Object key = entry.getKey();
                            final boolean isNotNum = !Util.isNumber(key);
                            String keyString = this.writeObjectAsString(key);
                            String valueString = this.writeObjectAsString(entry.getValue());
                            return new StringBuilder()
                                    .append(keyString)
                                    .insert(0, isNotNum ? "" : "\"")
                                    .append(isNotNum ? "" : "\"")
                                    .append(":")
                                    .append(valueString).toString();
                        }).collect(Collectors.joining(","))
                + "}";
    }
}
