package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator;

import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.base.Column;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.base.Table;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.base.ValueType;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.constraint.ConstraintSet;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.constraint.ForeignKey;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.constraint.PrimaryKey;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.rule.Rule;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.rule.RuleSet;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.query.QueryTool;

import java.sql.SQLException;
import java.util.*;

public class DatabaseGenerator {

    private List<TableGenerator> tableGenerators;
    private List<List<String>> tableHierarchy;
    private QueryTool queryTool;

    public DatabaseGenerator(QueryTool queryTool) {
        this.queryTool = queryTool;
    }

    public void fillDatabase(List<Table> tables, ConstraintSet constraintSet, RuleSet ruleSet) throws SQLException {
        tableGenerators = new ArrayList<>();
        for (Table table : tables) {
            queryTool.getQueryExecutor().executeQuery(getTableCreationQuery(table));
            tableGenerators.add(new TableGenerator(table.getName()));
        }
//        tableHierarchy = groupTablesByLevel(tableGenerators.stream().map(TableGenerator::getTableName).toList(), constraintSet.getForeignKeys());
//        for (PrimaryKey primaryKey : constraintSet.getPrimaryKeys()) {
//            Table table = tables.stream().filter(table1 -> table1.getName().equals(primaryKey.getTableName())).findFirst().get();
//            Column column = table.getColumns().stream().filter(c -> c.getName().equals(primaryKey.getTableName())).findFirst().get();
//            tableGenerators.stream()
//                    .filter(t -> t.getTableName().equals(primaryKey.getTableName()))
//                    .findFirst()
//                    .get()
//                    .getColumnGenerators()
//                    .add(PrimaryKeyGeneratorFactory.createColumnGenerator(primaryKey.getColumnName(), column.getType()));
//        }
//        List<Rule> allRules = new ArrayList<>();
//        allRules.addAll(ruleSet.getBooleanRules());
//        allRules.addAll(ruleSet.getIntegerRules());
//        allRules.addAll(ruleSet.getFloatRules());
//        allRules.addAll(ruleSet.getStringRules());
//        allRules.addAll(ruleSet.getDateRules());
//        for (Rule rule : allRules) {
//            TableGenerator tableGenerator = tableGenerators.stream().filter(table1 -> table1.getTableName().equals(rule.getTableName())).findFirst().get();
//            tableGenerator.getColumnGenerators().add(rule.toGenerator());
//        }
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

    private String getTableCreationQuery(Table table) {
        StringBuilder query = new StringBuilder("CREATE TABLE ");
        query.append(table.getName());
        query.append(" (");
        for (int i = 0; i < table.getColumns().size() - 1; i++) {
            Column column = table.getColumns().get(i);
            query.append(getColumnCreationQuery(column));
            query.append(", ");
        }
        query.append(getColumnCreationQuery(table.getColumns().getLast()));
        query.append(")");
        return query.toString();
    }

    private String getColumnCreationQuery(Column column) {
        StringBuilder query = new StringBuilder();
        if(column.getType() == ValueType.OBJECT_ID){
            query.append(column.getName());
            query.append(' ');
            query.append("VARCHAR");
        } else if(column.getType() == ValueType.INT_INTERVAL) {
            query.append(getIntervalCreationQuery(column.getName(), ValueType.INTEGER));
        } else if(column.getType() == ValueType.DATE_INTERVAL) {
            query.append(getIntervalCreationQuery(column.getName(), ValueType.DATE));
        } else {
            query.append(column.getName());
            query.append(' ');
            query.append(column.getType());
        }
        return query.toString();
    }

    private String getIntervalCreationQuery(String columnName, ValueType type) {
        return columnName +
                "Start " +
                type +
                ", " +
                columnName +
                "End " +
                type;
    }

}
