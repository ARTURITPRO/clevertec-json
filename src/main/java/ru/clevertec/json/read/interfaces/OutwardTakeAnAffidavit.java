package ru.clevertec.json.read.interfaces;

public interface OutwardTakeAnAffidavit {
    <T> T readStringToObject(String json, Class<T> obj);
}
