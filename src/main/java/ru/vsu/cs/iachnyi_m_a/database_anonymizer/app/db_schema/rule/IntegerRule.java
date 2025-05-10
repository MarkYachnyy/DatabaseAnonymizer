package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.rule;

import lombok.Data;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.distribution.discrete.DiscreteDistributionFactory;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator.ColumnGenerator;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator.IntegerGenerator;

import java.util.List;

@Data
public class IntegerRule implements Rule {
    private String tableName;
    private String columnName;
    private DiscreteDistributionType distributionType;
    private float[] params;
    private float nullChance;


    @Override
    public ColumnGenerator toGenerator() {
        return new IntegerGenerator(columnName, nullChance, DiscreteDistributionFactory.createDiscreteDistribution(distributionType, params));
    }
}
