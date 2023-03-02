package ru.clevertec.json.read;

import lombok.NoArgsConstructor;
import ru.clevertec.json.exception.ParsJsonException;
import ru.clevertec.json.read.interfaces.ClassTakeAnAffidavit;
import ru.clevertec.json.read.interfaces.OutwardTakeAnAffidavit;
import ru.clevertec.json.util.Util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@NoArgsConstructor
public class NumTakeAnAffidavit implements ClassTakeAnAffidavit {

    @Override
    public <T> T getObject(String s, Class<T> tClass, OutwardTakeAnAffidavit outwardTakeAnAffidavit) {

        if ("null".equals(s) || s == null)
            return null;

        final Number num;

        if (tClass == byte.class || tClass == Byte.class) num = Byte.parseByte(s);
        else if (tClass == short.class || tClass == Short.class) num = Short.parseShort(s);
        else if (tClass == int.class || tClass == Integer.class) num = Integer.parseInt(s);
        else if (tClass == long.class || tClass == Long.class) num = Long.parseLong(s);
        else if (tClass == float.class || tClass == Float.class) num = Float.parseFloat(s);
        else if (tClass == double.class || tClass == Double.class) num = Double.parseDouble(s);
        else if (tClass == BigDecimal.class) num = new BigDecimal(s);
        else if (tClass == BigInteger.class) num = new BigInteger(s);
        else if (tClass == AtomicInteger.class) num = new AtomicInteger(Integer.parseInt(s));
        else if (tClass == AtomicLong.class) num = new AtomicLong(Long.parseLong(s));
        else throw new ParsJsonException(String.format("%s not supported", tClass));

        return (T) num;
    }
    @Override
    public boolean canReadClass(Class<?> aClass) {
        return Util.isNumber(aClass);
    }

    public static NumTakeAnAffidavit getNumTakeAnAffidavit() {
        return new NumTakeAnAffidavit();
    }
}
