package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generation.generator.type_generator;

import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generation.distribution.discrete.DiscreteDistribution;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class StringGenerator extends ColumnGenerator{

    private final String columnName;
    private final float nullChance;
    private final List<String> allowedCharacters;
    private final DiscreteDistribution lengthDistribution;
    private final boolean unique;
    private final Random rand;
    private final List<Set<String>> alreadyGeneratedValues;

    public StringGenerator(String columnName, float nullChance, List<String> allowedCharacters, DiscreteDistribution lengthDistribution, boolean unique) {
        this.columnName = columnName;
        this.nullChance = nullChance;
        this.allowedCharacters = allowedCharacters;
        this.lengthDistribution = lengthDistribution;
        this.rand = new Random();
        this.unique = unique;
        this.alreadyGeneratedValues = List.of(new HashSet<>());
    }

    @Override
    public float getNullChance() {
        return nullChance;
    }

    @Override
    public String[] getColumnNames() {
        return new String[]{columnName};
    }


    @Override
    protected String[] generateValues() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("'");
        if(lengthDistribution != null){
            int length = lengthDistribution.next();
            for(int i = 0; i < length; i++){
                stringBuilder.append(allowedCharacters.get(0).charAt(rand.nextInt(allowedCharacters.get(0).length())));
            }
        } else {
            for (String allowedCharacter : allowedCharacters) {
                stringBuilder.append(allowedCharacter.charAt(rand.nextInt(allowedCharacter.length())));
            }
        }
        stringBuilder.append("'");
        return new String[]{stringBuilder.toString()};
    }

    @Override
    public boolean isUnique(){
        return unique;
    }

    @Override
    public List<Set<String>> getAlreadyGeneratedValues() {
        return alreadyGeneratedValues;
    }
}
