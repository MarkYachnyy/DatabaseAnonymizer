package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.generator;

import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.distribution.continuous.ContinuousDistribution;

public class FloatGenerator implements Generator{

    private final ContinuousDistribution distribution;

    public FloatGenerator(ContinuousDistribution distribution) {
        this.distribution = distribution;
    }

    @Override
    public Object generate() {
        return distribution.next();
    }
}
