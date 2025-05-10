package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.rule;

import lombok.Data;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.distribution.continuous.ContinuousDistributionFactory;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator.BooleanGenerator;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator.ColumnGenerator;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator.FloatGenerator;

@Data
public class BooleanRule implements Rule{
    private String tableName;
    private String columnName;
    private float trueChance;
    private float nullChance;

    @Override
    public ColumnGenerator toGenerator() {
        return new BooleanGenerator(columnName, nullChance, trueChance);
    }
}
