package ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask;

import java.util.Random;

public class RandomLengthWordMask implements Mask{

    int minLength;
    int maxLength;
    String allowedCharacters;
    Random random;

    public RandomLengthWordMask(int minLength, int maxLength, String allowedCharacters) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.allowedCharacters = allowedCharacters;
        random = new Random();
    }

    @Override
    public String generate() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < random.nextInt(minLength, maxLength + 1); i++) {
            res.append(allowedCharacters.charAt(random.nextInt(allowedCharacters.length())));
        }
        return res.toString();
    }
}
