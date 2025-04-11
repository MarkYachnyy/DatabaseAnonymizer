package ru.vsu.cs.iachnyi_m_a.database_scanner.app.db_schema.rule;

import lombok.Data;

import java.util.List;

@Data
public class StringRule {

    private List<String> allowedCharacters;
    private IntegerRule lengthRule;
}
