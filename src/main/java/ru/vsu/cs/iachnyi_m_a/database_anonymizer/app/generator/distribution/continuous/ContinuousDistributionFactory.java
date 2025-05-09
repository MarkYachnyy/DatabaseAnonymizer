package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.distribution.continuous;

import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.rule.ContinuousDistributionType;

public class ContinuousDistributionFactory {
    public static ContinuousDistribution createContinuousDistribution(ContinuousDistributionType type, float[] params){
        return switch (type){
            case NORMAL -> new NormalDistribution(params[0], params[1]);
            case EXPONENTIAL -> new ExponentialDistribution(params[0]);
            case UNIFORM -> new ContinuousUniformDistribution(params[0], params[1]);
        };
    }
}
