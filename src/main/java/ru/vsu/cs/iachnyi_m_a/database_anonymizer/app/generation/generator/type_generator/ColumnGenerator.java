package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generation.generator.type_generator;

import java.util.List;
import java.util.Set;

public abstract class ColumnGenerator {
    public abstract float getNullChance();
    public abstract String[] getColumnNames();
    public String[] getNextValues(){
        if(!isUnique()){
            return generateValues();
        } else {
            String[] res = generateValues();
            boolean noUniques = true;
            for (int i = 0; i < res.length; i++) {
                if(!getAlreadyGeneratedValues().get(i).contains(res[i])) {
                    noUniques = false;
                    break;
                }
            }
            while (noUniques) {
                res = generateValues();
                for (int i = 0; i < res.length; i++) {
                    if(!getAlreadyGeneratedValues().get(i).contains(res[i])) {
                        noUniques = false;
                        break;
                    }
                }
            }
            for (int i = 0; i < res.length; i++) {
                getAlreadyGeneratedValues().get(i).add(res[i]);
            }
            return res;
        }
    };
    protected abstract String[] generateValues();
    public abstract boolean isUnique();
    public abstract List<Set<String>> getAlreadyGeneratedValues();
}
