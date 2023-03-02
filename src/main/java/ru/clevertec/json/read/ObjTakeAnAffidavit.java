package ru.clevertec.json.read;

import ru.clevertec.json.exception.ParsJsonException;
import ru.clevertec.json.parse.ParseObj;
import ru.clevertec.json.read.interfaces.ClassTakeAnAffidavit;
import ru.clevertec.json.read.interfaces.OutwardTakeAnAffidavit;
import ru.clevertec.json.util.Util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

public class ObjTakeAnAffidavit implements ClassTakeAnAffidavit {

    private final ParseObj parseObj;

    private ObjTakeAnAffidavit() {
        this.parseObj = ParseObj.getInstance();
    }

    @Override
    public <T> T getObject(String s, Class<T> tClass, OutwardTakeAnAffidavit outwardTakeAnAffidavit) {
        if ("null".equals(s) || s == null)
            return null;
        final Object o = Util.getInstance(tClass);
        final Map<String, String> stringMap = parseObj.parseJson(s);
        final Map<Field, String> fields = Util.getAllDeclaredFields(tClass).stream()
                .filter(field -> stringMap.containsKey(field.getName()))
                .map(f -> {f.setAccessible(true);return f;
                }).collect(Collectors.toMap(f -> f, f -> stringMap.get(f.getName())));

        fields.entrySet().stream().filter(e -> !"null".equals(e.getValue())).filter(e -> e.getKey().getGenericType() instanceof ParameterizedType)
                .filter(e -> Util.isClassInstanceOf(e.getKey().getType(), Collection.class)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                .forEach((key, value) -> {
                    final Class<?> type = key.getType();
                    final ParameterizedType type1 = (ParameterizedType)key.getGenericType();
                    final Object o1;
                    o1 = getCollection(type, type1, value, outwardTakeAnAffidavit);
                    setField(key, o, o1);
                });

        fields.entrySet().stream().filter(e -> Util.isClassInstanceOf(e.getKey().getType(), Map.class))
                .filter(e -> !"null".equals(e.getValue())).filter(e -> e.getKey().getGenericType() instanceof ParameterizedType)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).forEach((key, value) -> {
                    final ParameterizedType type = (ParameterizedType)key.getGenericType();
                    final Object o1;
                    o1 = getMap(type, value, outwardTakeAnAffidavit);
                    setField(key, o, o1);
                });

        fields.entrySet().stream()
                .filter(e -> !(e.getKey().getGenericType() instanceof ParameterizedType)).forEach(fieldJson -> {
                    final Class<?> type = fieldJson.getKey().getType();
                    final String s1 = fieldJson.getValue();
                    final Object o1 = outwardTakeAnAffidavit.readStringToObject(s1, type);
                    setField(fieldJson.getKey(), o, o1);
            });

        return (T) o;
    }
    @Override
    public boolean canReadClass(Class<?> aClass) {
        return Arrays.stream(aClass.getDeclaredConstructors()).anyMatch(c -> c.getParameterCount() == 0);
    }

    private Collection<?> getCollection(Class<?> aClass, ParameterizedType type, String s, OutwardTakeAnAffidavit outwardTakeAnAffidavit) {
        final Class<?> aClass1 = (Class<?>) type.getActualTypeArguments()[0];
        final Collection<?> coll;
        Object[] objects;
        if (Util.isClassInstanceOf(aClass, List.class)) {
            objects = (Object[]) outwardTakeAnAffidavit.readStringToObject(s, aClass1.arrayType());
            coll = Arrays.stream(objects).collect(Collectors.toList());
        }else if (Util.isClassInstanceOf(aClass, Set.class)){
            objects = (Object[]) outwardTakeAnAffidavit.readStringToObject(s, aClass1.arrayType());
            coll = Arrays.stream(objects).collect(Collectors.toSet());
        }else {
            throw new ParsJsonException("Unsupported type: " + aClass.getName());
        }
        return coll;
    }

    private Map<?,?> getMap(ParameterizedType type, String json, OutwardTakeAnAffidavit reader) {
        final Class<?> aClass = (Class<?>) type.getActualTypeArguments()[0];
        final Class<?> aClass1 = (Class<?>) type.getActualTypeArguments()[1];
        final Map<Object, Object> objectHashMap = new HashMap<>();
        final Map<String, String> stringMap = parseObj.parseJson(json);

        stringMap.forEach((k, v) -> { if (!Util.isNumber(aClass)) {k = "\"" + k + "\"";}
            Object o = reader.readStringToObject(k, aClass);
            Object o1 = reader.readStringToObject(v, aClass1);
            objectHashMap.put(o, o1);
        });
        return objectHashMap;
    }

    private void setField(Field f, Object o, Object o1) {
        try {f.set(o, o1);} catch (IllegalAccessException e) { throw new ParsJsonException(e);}
    }

    public static ObjTakeAnAffidavit getObjTakeAnAffidavit() {
        return new ObjTakeAnAffidavit();
    }
}
