package ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask;

import java.util.List;
import java.util.Random;

public class FixedLengthWordMask implements Mask<String>{

    private List<String> allowedCharacters;
    Random random;

    public FixedLengthWordMask(List<String> allowedCharacters) {
        this.allowedCharacters = allowedCharacters;
        random = new Random();
    }

    @Override
    public String generate() {
        StringBuilder result = new StringBuilder();
        for(String chars: allowedCharacters) {
            result.append(chars.charAt(random.nextInt(chars.length())));
        }
        return result.toString();
    }
}
