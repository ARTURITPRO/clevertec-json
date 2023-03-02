package ru.clevertec.json.read.interfaces;

public interface ClassTakeAnAffidavit {
    boolean canReadClass(Class<?> clazz);
    <T> T getObject(String json, Class<T> obj, OutwardTakeAnAffidavit reader);


}
