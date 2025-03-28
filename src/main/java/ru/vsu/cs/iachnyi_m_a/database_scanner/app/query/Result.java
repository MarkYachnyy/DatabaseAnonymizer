package ru.vsu.cs.iachnyi_m_a.database_scanner.app.query;

import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
public class Result {

    private Map<String, Object> content;

    public Result() {
        this.content = new HashMap<String, Object>();
    }

    public int getInteger(String columnName){
        return (Integer) content.get(columnName);
    }
    public String getString(String columnName){
        return (String) content.get(columnName);
    }
    public float getFloat(String columnName){
        return (Float) content.get(columnName);
    }
    public long getLong(String columnName){
        return (Long) content.get(columnName);
    }
    public Object getObject(String columnName){return content.get(columnName);}
    public void put(String columnName, Object value){
        content.put(columnName, value);
    }
}
