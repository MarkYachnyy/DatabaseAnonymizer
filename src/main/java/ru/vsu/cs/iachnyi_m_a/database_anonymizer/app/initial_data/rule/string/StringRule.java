package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.initial_data.rule.string;

import lombok.Data;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generation.distribution.discrete.DiscreteDistributionFactory;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generation.generator.type_generator.ColumnGenerator;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generation.generator.type_generator.StringGenerator;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.initial_data.rule.IntegerRule;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.initial_data.rule.Rule;

import java.util.List;

@Data
public class StringRule implements Rule {
    private String tableName;
    private String columnName;
    private List<String> allowedCharacters;
    private IntegerRule lengthRule;
    private float nullChance;

    @Override
    public ColumnGenerator toGenerator(boolean unique) {
        return new StringGenerator(
                columnName,
                nullChance,
                allowedCharacters,
                lengthRule == null ? null :
                DiscreteDistributionFactory.createDiscreteDistribution(lengthRule.getDistributionType(), lengthRule.getParams()), unique);
    }
}
