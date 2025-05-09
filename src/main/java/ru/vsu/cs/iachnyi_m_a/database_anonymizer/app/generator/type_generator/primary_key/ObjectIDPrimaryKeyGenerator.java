package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator.primary_key;

import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.distribution.discrete.DiscreteUniformDistribution;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator.ColumnGenerator;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator.StringGenerator;

import java.util.List;

public class ObjectIDPrimaryKeyGenerator implements ColumnGenerator {

    private final String columnName;
    private final StringGenerator stringGenerator;

    public ObjectIDPrimaryKeyGenerator(String columnName) {
        this.columnName = columnName;
        this.stringGenerator = new StringGenerator(null, 0, List.of("0123456789abcdef"), new DiscreteUniformDistribution(24, 25));
    }

    @Override
    public float getNullChance() {
        return 0;
    }

    @Override
    public String[] getColumnNames() {
        return new String[]{columnName};
    }

    @Override
    public String[] getNextValues() {
        return new String[]{stringGenerator.getNextValues()[0]};
    }
}
