package ru.clevertec.json.read;

import ru.clevertec.json.read.interfaces.ClassTakeAnAffidavit;
import ru.clevertec.json.read.interfaces.OutwardTakeAnAffidavit;

public class StrTakeAnAffidavit implements ClassTakeAnAffidavit {

    @Override
    public <T> T getObject(String s, Class<T> tClass, OutwardTakeAnAffidavit outwardTakeAnAffidavit) {
        if ("null".equals(s) || s == null)
            return null;
        return tClass.cast(s.substring(1, s.length() - 1));
    }

    @Override
    public boolean canReadClass(Class<?> aClass) {
        return aClass == String.class;
    }
    public static StrTakeAnAffidavit getStrTakeAnAffidavit() {
        return new StrTakeAnAffidavit();
    }
}
