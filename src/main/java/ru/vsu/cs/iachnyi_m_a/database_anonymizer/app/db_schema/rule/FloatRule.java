package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.rule;

import lombok.Data;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.distribution.continuous.ContinuousDistributionFactory;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator.ColumnGenerator;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator.FloatGenerator;

import java.util.List;

@Data
public class FloatRule implements Rule {
    private String tableName;
    private String columnName;

    private ContinuousDistributionType distributionType;
    private float[] params;
    private float nullChance;

    @Override
    public ColumnGenerator toGenerator() {
        return new FloatGenerator(
                columnName,
                nullChance,
                ContinuousDistributionFactory.createContinuousDistribution(distributionType, params));
    }
}
