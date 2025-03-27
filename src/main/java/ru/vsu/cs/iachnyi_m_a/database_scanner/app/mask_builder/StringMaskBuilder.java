package ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask_builder;

import lombok.ToString;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask.Mask;

import java.util.*;
import java.util.stream.Collectors;

@ToString
public class StringMaskBuilder implements MaskBuilder {

    private int minLength;
    private int maxLength;
    private List<Set<Character>> characters;
    private Set<String> enumValues;
    private int maxEnumValueCount;

    public StringMaskBuilder(int maxEnumValueCount) {
        this.maxEnumValueCount = maxEnumValueCount;
        this.enumValues = new HashSet<>();
        this.characters = new ArrayList<>();
        this.minLength = 0;
        this.maxLength = 0;
    }

    @Override
    public void append(Object value) {
        if(!(value instanceof String)){
            throw new IllegalArgumentException();
        }
        String strValue = (String)value;
        if (characters.isEmpty() && enumValues.isEmpty()) {
            enumValues.add(strValue);
        } else {
            if (!enumValues.isEmpty()) {
                appendEnumValue(strValue);
            } else if (!(characters.size() == 1 && maxLength != minLength)) {
                appendFixedLengthWord(strValue);
            } else {
                appendRandomLengthWord(strValue);
            }
        }
    }

    private void appendEnumValue(String value) {
        if (enumValues.size() == maxEnumValueCount) {
            maxLength = value.length();
            minLength = value.length();
            long lengthCount = enumValues.stream().map(String::length).filter(a -> a == value.length()).count();
            if (lengthCount != enumValues.size()) {
                characters.add(new HashSet<>());
                for (String enumValue : enumValues) {
                    appendRandomLengthWord(enumValue);
                }
                appendRandomLengthWord(value);
            } else {
                for (int i = 0; i < value.length(); i++) {
                    characters.add(new HashSet<>());
                }
                for (String enumValue : enumValues) {
                    appendFixedLengthWord(enumValue);
                }
                appendFixedLengthWord(value);
            }
            enumValues.clear();
        } else {
            enumValues.add(value);
        }
    }

    private void appendFixedLengthWord(String value) {
        if (value.length() != minLength) {
            minLength = Math.min(value.length(), minLength);
            maxLength = Math.max(value.length(), maxLength);
            for (int i = 1; i < characters.size(); i++) {
                for (char c : characters.get(i)) {
                    characters.getFirst().add(c);
                }
            }
            Set<Character> firstSet = characters.getFirst();
            characters.clear();
            characters.add(firstSet);
        } else {
            for (int i = 0; i < minLength; i++) {
                characters.get(i).add(value.charAt(i));
            }
        }
    }

    ;

    private void appendRandomLengthWord(String value) {
        minLength = Math.min(value.length(), minLength);
        maxLength = Math.max(value.length(), maxLength);
        for (char c : value.toCharArray()) {
            characters.getFirst().add(c);
        }
    }

    @Override
    public Mask<String> get() {
        return null;
    }
}
