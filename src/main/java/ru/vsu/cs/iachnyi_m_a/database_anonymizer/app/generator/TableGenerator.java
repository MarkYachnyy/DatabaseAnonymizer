package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator;

import lombok.Data;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.constraint.ConstraintSet;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.constraint.ForeignKey;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator.ColumnGenerator;

import java.util.*;

@Data
public class TableGenerator {
    private String tableName;
    private List<ColumnGenerator> columnGenerators;

    public TableGenerator(String tableName) {
        this.tableName = tableName;
        this.columnGenerators = new ArrayList<>();
    }
}
