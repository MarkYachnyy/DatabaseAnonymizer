package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator;

import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.distribution.discrete.DiscreteDistribution;

import java.util.List;
import java.util.Random;

public class StringGenerator implements ColumnGenerator{

    private final String columnName;
    private final float nullChance;
    private final List<String> allowedCharacters;
    private final DiscreteDistribution lengthDistribution;
    private final Random rand;

    public StringGenerator(String columnName, float nullChance, List<String> allowedCharacters, DiscreteDistribution lengthDistribution) {
        this.columnName = columnName;
        this.nullChance = nullChance;
        this.allowedCharacters = allowedCharacters;
        this.lengthDistribution = lengthDistribution;
        this.rand = new Random();
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
    public String[] getNextValues() {
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
}
