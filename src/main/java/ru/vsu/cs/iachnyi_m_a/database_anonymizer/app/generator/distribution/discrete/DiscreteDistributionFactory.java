package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.distribution.discrete;

import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.rule.DiscreteDistributionType;

public class DiscreteDistributionFactory {
    public static DiscreteDistribution createDiscreteDistribution(DiscreteDistributionType type, float[] params){
        return switch (type){
            case UNIFORM -> new DiscreteUniformDistribution((int) params[0], (int) params[1]);
            case POISSON -> new PoissonDistribution(params[0]);
            case BINOMIAL -> new BinomialDistribution((int) params[0], params[1]);
        };
    }
}
