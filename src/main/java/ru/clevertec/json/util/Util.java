package ru.clevertec.json.util;

import ru.clevertec.json.exception.ParsJsonException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;


public class Util {
    private Util() {}

    public static boolean isNumber(Class<?> aClass) {
        return aClass != null &&
                aClass.isPrimitive() && aClass != char.class ||
                isClassInstanceOf(aClass, Number.class);
    }

    /**
     * Check if an object is a number
     * @param o checking object
     * @return result
     */
    public static boolean isNumber(Object o) {
        if (o == null) return false;
        Class<?> aClass = o.getClass();
        return  isNumber(aClass);
    }

    /**
     * Return nesting of an array
     * @param c array class
     * @return if "c" is array -> number of array nesting else return 0
     */
    public static int getArrayIndex(Class<?> c) {
        if (!c.isArray()) return 0;
        Class<?> componentType = c.getComponentType();
        return 1 + getArrayIndex(componentType);
    }

    /**
     * Get class plus superclasses declared fields
     * @param aClass current class
     * @return List of all declared fields
     */
    public static List<Field> getAllDeclaredFields(Class<?> aClass) {
        if (aClass == null) return Collections.emptyList();
        final Class<?> superclass = aClass.getSuperclass();
        return Stream.concat(Arrays.stream(aClass.getDeclaredFields()), getAllDeclaredFields(superclass).stream())
                .toList();
    }

    /**
     * Find all interfaces in a class and superclasses
     * @param aClass class where to look
     * @return List of interfaces
     */
    public static List<Class<?>> getAllInterfaces(Class<?> aClass) {
        if (aClass == null) return Collections.emptyList();
        final Class<?> superclass = aClass.getSuperclass();
        return Stream.concat(
                        aClass.isInterface() ? Stream.of(aClass) : Stream.empty(), Stream.concat(
                                Arrays.stream(aClass.getInterfaces()), getAllInterfaces(superclass).stream()))
                .toList();
    }

    /**
     * Check if the current class instance of the given class
     * @param cClass current class
     * @param ofClass given class
     * @return cClass instanceOf ofClass
     */
    public static boolean isClassInstanceOf(Class<?> cClass, Class<?> ofClass) {
        if (cClass == ofClass) return true;
        if (cClass == null || ofClass == null) return false;
        Class<?> superclass = cClass.getSuperclass();
        return isClassInstanceOf(superclass, ofClass) ||
                getAllInterfaces(cClass).stream()
                        .anyMatch(x -> x == ofClass);
    }

    /**
     * Get object of class
     * @param aClass current class
     * @return null if class = null, else object
     * @throws ParsJsonException if class don't have no args constructor
     */
    public static Object getInstance(Class<?> aClass) throws ParsJsonException {
        if (aClass == null) return null;
        final Constructor<?> constructor = Arrays.stream(aClass.getDeclaredConstructors())
                .filter(c -> c.getParameterCount() == 0)
                .findFirst()
                .orElseThrow(() -> new ParsJsonException(String.format("CLass: %s must have no args constructor", aClass.getName())));
        constructor.setAccessible(true);

        try {
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ParsJsonException(e.getMessage());
        }
    }

    public static boolean isPrimitive(Class<?> c) {
        return isNumber(c) || c == char.class || c == Character.class;
    }
}
