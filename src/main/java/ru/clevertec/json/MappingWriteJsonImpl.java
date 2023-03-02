package ru.clevertec.json;

import ru.clevertec.json.mapping.interfaces.MappingWriteJson;
import ru.clevertec.json.read.OutwardTakeAnAffidavitImpl;
import ru.clevertec.json.read.interfaces.OutwardTakeAnAffidavit;
import ru.clevertec.json.write.WriteJsonImpl;
import ru.clevertec.json.write.interfaces.WriteJson;

public class MappingWriteJsonImpl implements MappingWriteJson {

    private final WriteJson writeJson;
    private final OutwardTakeAnAffidavit outwardTakeAnAffidavit;

    public MappingWriteJsonImpl() {
        writeJson = new WriteJsonImpl();
        outwardTakeAnAffidavit = new OutwardTakeAnAffidavitImpl();
    }

    @Override
    public String writeObjectAsString(Object value) {
        return writeJson.writeObjectAsString(value);
    }

    @Override
    public <T> T readStringToObject(String json, Class<T> obj) {
        return outwardTakeAnAffidavit.readStringToObject(json, obj);
    }
}
