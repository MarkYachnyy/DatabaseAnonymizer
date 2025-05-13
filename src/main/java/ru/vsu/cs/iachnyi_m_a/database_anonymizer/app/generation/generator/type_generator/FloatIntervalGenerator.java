package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generation.generator.type_generator;

import java.util.HashSet;
import java.util.Set;

public class FloatIntervalGenerator extends ColumnGenerator {

    private String intervalName;
    private float nullChance;
    private float startMin;
    private float startMax;
    private float endMin;
    private float endMax;
    private boolean unique;

    private Set<Integer> alreadyGeneratedValues;

    public FloatIntervalGenerator(String intervalName, float nullChance, float startMin, float startMax, float endMin, float endMax, boolean unique) {
        this.intervalName = intervalName;
        this.nullChance = nullChance;
        this.startMin = startMin;
        this.startMax = startMax;
        this.endMin = endMin;
        this.endMax = endMax;
        this.unique = unique;
        this.alreadyGeneratedValues = new HashSet<Integer>();
    }

    @Override
    public float getNullChance() {
        return nullChance;
    }

    @Override
    public String[] getColumnNames() {
        return new String[]{intervalName+"_start", intervalName+"_end"};
    }

    @Override
    protected String[] generateValues() {
        return new String[0];
    }

    @Override
    public boolean isUnique() {
        return false;
    }

    @Override
    public Set<Integer> getAlreadyGeneratedValuesHashCodes() {
        return alreadyGeneratedValues;
    }
}
