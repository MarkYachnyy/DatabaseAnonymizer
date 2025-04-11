package ru.vsu.cs.iachnyi_m_a.database_scanner.app.generator.generator;

import ru.vsu.cs.iachnyi_m_a.database_scanner.app.generator.distribution.discrete.DiscreteDistribution;

import java.util.List;
import java.util.Random;

public class StringGenerator implements Generator{

    private List<String> allowedCharacters;
    private DiscreteDistribution lengthDistribution;
    private Random rand;

    public StringGenerator(List<String> allowedCharacters, DiscreteDistribution lengthDistribution) {
        this.allowedCharacters = allowedCharacters;
        this.lengthDistribution = lengthDistribution;
        this.rand = new Random();
    }


    @Override
    public Object generate() {
        StringBuilder stringBuilder = new StringBuilder();
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
        return stringBuilder.toString();
    }
}
