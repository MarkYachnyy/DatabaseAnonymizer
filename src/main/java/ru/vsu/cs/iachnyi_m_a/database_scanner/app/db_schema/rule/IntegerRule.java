package ru.vsu.cs.iachnyi_m_a.database_scanner.app.db_schema.rule;

import lombok.Data;

import java.util.List;

@Data
public class IntegerRule {
    private DiscreteDistributionType distributionType;
    private List<Float> params;
}
