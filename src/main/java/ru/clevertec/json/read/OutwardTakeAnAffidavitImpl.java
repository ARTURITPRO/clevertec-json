package ru.clevertec.json.read;

import ru.clevertec.json.exception.ParsJsonException;
import ru.clevertec.json.read.interfaces.ClassTakeAnAffidavit;
import ru.clevertec.json.read.interfaces.OutwardTakeAnAffidavit;

import java.util.ArrayList;
import java.util.List;

public class OutwardTakeAnAffidavitImpl implements OutwardTakeAnAffidavit {
    private final List<ClassTakeAnAffidavit> clasTakeAnAffidavit;

    public OutwardTakeAnAffidavitImpl() {
        clasTakeAnAffidavit = new ArrayList<>();
        clasTakeAnAffidavit.add(NumTakeAnAffidavit.getNumTakeAnAffidavit());
        clasTakeAnAffidavit.add(CharTakeAnAffidavit.getCharTakeAnAffidavit());
        clasTakeAnAffidavit.add(StrTakeAnAffidavit.getStrTakeAnAffidavit());
        clasTakeAnAffidavit.add(ArrTakeAnAffidavitImpl.getArrTakeAnAffidavit());
        clasTakeAnAffidavit.add(ObjTakeAnAffidavit.getObjTakeAnAffidavit());
    }

    @Override
    public <T> T readStringToObject(String s, Class<T> obj) {
        if ("null".equals(s))
            return null;
        return clasTakeAnAffidavit.stream().filter(classReader -> classReader.canReadClass(obj)).findFirst()
                .map(classReader -> classReader.getObject(s, obj, this)).orElseThrow(() ->
                        new ParsJsonException(String.format("not read %s", obj.getName())));
    }
}
