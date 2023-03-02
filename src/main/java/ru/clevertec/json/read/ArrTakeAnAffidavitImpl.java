package ru.clevertec.json.read;

import lombok.NoArgsConstructor;
import ru.clevertec.json.read.interfaces.ClassTakeAnAffidavit;
import ru.clevertec.json.read.interfaces.OutwardTakeAnAffidavit;
import ru.clevertec.json.util.Util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor
public class ArrTakeAnAffidavitImpl implements ClassTakeAnAffidavit {

    public static ArrTakeAnAffidavitImpl getArrTakeAnAffidavit() {
        return new ArrTakeAnAffidavitImpl();
    }

    @Override
    public <T> T getObject(String str, Class<T> o, OutwardTakeAnAffidavit outwardTakeAnAffidavit) {
        if ("null".equals(str) || str == null)
            return null;

        final T output;

        final Class<?> type = o.getComponentType();

        final Matcher matcher;

        final String s1;


        if (type.isArray()) {
            final int quotes = Util.getArrayIndex(o) - 1;
            s1 = str.substring(1);
            matcher = Pattern.compile("(\\[{" + quotes + "}).+?(]{" + quotes + "})")
                    .matcher(s1);

            output = o.cast(getObjectsOrArraysArray(matcher, type, outwardTakeAnAffidavit));
        } else if (!(Util.isPrimitive(type) || type == String.class)) {

            s1 = str.substring(1, str.length() - 1);

            matcher = Pattern.compile("\\{.+?}").matcher(s1);

            return o.cast(getObjectsOrArraysArray(matcher, type, outwardTakeAnAffidavit));

        } else {
            s1 = str.replaceAll("[\\[\\]]", "");
            final String[] array = s1.split(",");
            output = o.cast(
                    Arrays.stream(array).map(s -> outwardTakeAnAffidavit.readStringToObject(s, type))
                            .toList().toArray(getArrayInstance(type, array.length)));
        }
        return output;
    }

    @Override
    public boolean canReadClass(Class<?> aClass) {
        return aClass.isArray();
    }

    private <T> T getObjectsOrArraysArray(Matcher m, Class<T> tClass,
                                          OutwardTakeAnAffidavit outwardTakeAnAffidavit) {
        final int length = (int) m.results().count();
        m.reset();
        final T[] outputArr = m.results().map(MatchResult::group).map(r -> outwardTakeAnAffidavit.readStringToObject(r, tClass))
                .toList().toArray(getArrayInstance(tClass, length));
        return (T) outputArr;
    }
    private <T> T[] getArrayInstance(Class<T> componentType, int length) {
         return (T[]) Array.newInstance(componentType, length);
    }

}
