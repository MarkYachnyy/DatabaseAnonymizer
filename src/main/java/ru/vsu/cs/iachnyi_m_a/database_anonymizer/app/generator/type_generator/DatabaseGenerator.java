package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator;

import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.base.Table;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.constraint.ConstraintSet;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.constraint.ForeignKey;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.TableGenerator;

import java.util.*;

public class DatabaseGenerator {

    private final List<TableGenerator> tableGenerators;
    private List<List<String>> tableHierarchy;

    public DatabaseGenerator(List<Table> tables) {
        tableGenerators = new ArrayList<TableGenerator>();
        for (Table table : tables) {
            tableGenerators.add(new TableGenerator(table.getName()));
        }
    }

    public void addConstraintSet(ConstraintSet constraintSet) {
        tableHierarchy = groupTablesByLevel(tableGenerators.stream().map(TableGenerator::getTableName).toList(), constraintSet.getForeignKeys());
    }

    private List<List<String>> groupTablesByLevel(List<String> allTables, List<ForeignKey> foreignKeys) {
        Map<String, Set<String>> dependencyMap = new HashMap<>();

        for (String table : allTables) {
            dependencyMap.put(table, new HashSet<>());
        }

        for (ForeignKey fk : foreignKeys) {
            dependencyMap.get(fk.getTargetTableName()).add(fk.getSourceTableName());
        }

        Map<String, Integer> tableLevels = new HashMap<>();

        List<List<String>> result = new ArrayList<>();

        while (tableLevels.size() < allTables.size()) {
            List<String> currentLevelTables = new ArrayList<>();

            for (String table : allTables) {
                if (tableLevels.containsKey(table)) {
                    continue;
                }

                boolean allDependenciesResolved = true;
                for (String dependentTable : dependencyMap.get(table)) {
                    if (!tableLevels.containsKey(dependentTable)) {
                        allDependenciesResolved = false;
                        break;
                    }
                }

                if (allDependenciesResolved) {
                    int level = 0;
                    for (String dependentTable : dependencyMap.get(table)) {
                        level = Math.max(level, tableLevels.get(dependentTable));
                    }
                    tableLevels.put(table, level + 1);
                    currentLevelTables.add(table);
                }
            }

            if (currentLevelTables.isEmpty()) {
                for (String table : allTables) {
                    if (!tableLevels.containsKey(table) && dependencyMap.get(table).isEmpty()) {
                        tableLevels.put(table, 1);
                        currentLevelTables.add(table);
                    }
                }

                if (currentLevelTables.isEmpty()) {
                    for (String table : allTables) {
                        if (!tableLevels.containsKey(table)) {
                            tableLevels.put(table, 1);
                            currentLevelTables.add(table);
                        }
                    }
                }
            }

            if (!currentLevelTables.isEmpty()) {
                while (result.size() <= tableLevels.get(currentLevelTables.get(0))) {
                    result.add(new ArrayList<>());
                }
                result.get(tableLevels.get(currentLevelTables.get(0))).addAll(currentLevelTables);
            }
        }

        List<List<String>> finalResult = new ArrayList<>();
        for (List<String> level : result) {
            if (!level.isEmpty()) {
                finalResult.add(level);
            }
        }

        return finalResult;
    }
}
