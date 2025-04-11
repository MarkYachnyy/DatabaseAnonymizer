package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.generator;

import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.distribution.discrete.DiscreteDistribution;

public class IntegerGenerator implements Generator {
    private final DiscreteDistribution distribution;

    public IntegerGenerator(DiscreteDistribution distribution) {
        this.distribution = distribution;
    }

    @Override
    public Object generate() {
        return distribution.next();
    }
}
