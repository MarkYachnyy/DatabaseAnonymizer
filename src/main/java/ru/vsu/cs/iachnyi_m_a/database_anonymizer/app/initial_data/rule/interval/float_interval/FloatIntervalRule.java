package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.initial_data.rule.interval.float_interval;

import lombok.Data;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generation.generator.type_generator.ColumnGenerator;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.initial_data.rule.Rule;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.initial_data.rule.interval.AllenAlgebraRelation;

import java.util.Map;

@Data
public class FloatIntervalRule implements Rule {

    private String tableName;
    private String columnName;

    private float nullChance;
    private Map<FloatInterval, AllenAlgebraRelation> relations;

    @Override
    public ColumnGenerator toGenerator(boolean unique) {
        return null;
    }
}
