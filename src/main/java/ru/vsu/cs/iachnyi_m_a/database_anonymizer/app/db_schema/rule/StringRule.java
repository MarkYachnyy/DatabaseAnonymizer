package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.rule;

import lombok.Data;

import java.util.List;

@Data
public class StringRule {
    private String tableName;
    private String columnName;
    private List<String> allowedCharacters;
    private IntegerRule lengthRule;
    private float nullChance;
}
