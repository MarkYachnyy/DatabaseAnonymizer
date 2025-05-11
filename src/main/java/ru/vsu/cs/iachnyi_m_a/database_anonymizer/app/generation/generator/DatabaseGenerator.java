package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generation.generator;

import org.postgresql.util.PSQLException;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generation.distribution.discrete.DiscreteDistribution;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generation.distribution.discrete.DiscreteDistributionFactory;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.initial_data.rule.DiscreteDistributionType;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.initial_data.schema.Column;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.initial_data.schema.DatabaseSchema;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.initial_data.schema.Table;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.initial_data.schema.ValueType;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.initial_data.constraint.ConstraintSet;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.initial_data.constraint.ForeignKey;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.initial_data.constraint.PrimaryKey;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.initial_data.rule.Rule;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.initial_data.rule.RuleSet;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generation.generator.primary_key_generator.PrimaryKeyGeneratorFactory;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generation.graph.RelationMapElement;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generation.graph.TableRelationGraph;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generation.graph.TableRelationGraphNode;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generation.query.QueryExecutor;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generation.query.QueryTool;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generation.generator.type_generator.ColumnGenerator;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class DatabaseGenerator {

    private List<TableGenerator> tableGenerators;
    private TableRelationGraph tableRelationGraph;
    private final QueryTool queryTool;

    public DatabaseGenerator(QueryTool queryTool) {
        this.queryTool = queryTool;
    }

    public void fillDatabase(DatabaseSchema schema, ConstraintSet constraintSet, RuleSet ruleSet, String firstTableName, int firstTableCount) throws SQLException {

        //СОЗДАНИЕ ТАБЛИЦ

        List<Table> tables = schema.getTables();
        tableGenerators = new ArrayList<>();
        for (Table table : tables) {
            try {
                queryTool.getQueryExecutor().executeQuery("DROP TABLE " + table.getName() + ";");
            } catch (PSQLException e) {

            }
            queryTool.getQueryExecutor().executeQuery(getTableCreationQuery(table));
            tableGenerators.add(new TableGenerator(table.getName()));
        }

        //ЗАПОЛНЕНИЕ ГРАФА ОТНОШЕНИЯ ТАБЛИЦ

        tableRelationGraph = getTableRelationGraph(schema.getTables().stream().map(Table::getName).toList(), constraintSet.getForeignKeys());

        //ДОБАВЛЕНИЕ ГЕНЕРАТОРОВ ПЕРВИЧНЫХ КЛЮЧЕЙ

        for (PrimaryKey primaryKey : constraintSet.getPrimaryKeys()) {
            Table table = tables.stream().filter(table1 -> table1.getName().equals(primaryKey.getTableName())).findFirst().get();
            Column column = table.getColumns().stream().filter(c -> c.getName().equals(primaryKey.getColumnName())).findFirst().get();
            tableGenerators.stream()
                    .filter(t -> t.getTableName().equals(primaryKey.getTableName()))
                    .findFirst()
                    .get()
                    .setPrimaryKeyGenerator(PrimaryKeyGeneratorFactory.createColumnGenerator(primaryKey.getColumnName(), column.getType()));
        }

        //ДОБАВЛЕНИЕ ГЕНЕРАТОРОВ ОБЫЧНЫХ ЗНАЧЕНИЙ

        List<Rule> allRules = new ArrayList<>();
        if (ruleSet.getBooleanRules() != null) allRules.addAll(ruleSet.getBooleanRules());
        if (ruleSet.getIntegerRules() != null) allRules.addAll(ruleSet.getIntegerRules());
        if (ruleSet.getFloatRules() != null) allRules.addAll(ruleSet.getFloatRules());
        if (ruleSet.getStringRules() != null) allRules.addAll(ruleSet.getStringRules());
        if (ruleSet.getDateRules() != null) allRules.addAll(ruleSet.getDateRules());
        for (Rule rule : allRules) {
            TableGenerator tableGenerator = tableGenerators.stream().filter(table1 -> table1.getTableName().equals(rule.getTableName())).findFirst().get();
            tableGenerator.getColumnGenerators().add(rule.toGenerator());
            System.out.println(Arrays.toString(tableGenerator.getColumnGenerators().toArray()));
        }

        //ЗАПУСК ЗАПОЛНЕНИЯ ТАБЛИЦ

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(schema.getTables().size(), schema.getTables().size(), 2, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        fillFirstTable(tableRelationGraph.findNodeByTableName(firstTableName),
                threadPoolExecutor,
                queryTool,
                tableGenerators.stream().filter(tg -> tg.getTableName().equals(firstTableName)).findFirst().get(),
                firstTableCount);
    }

    private TableRelationGraph getTableRelationGraph(List<String> allTables, List<ForeignKey> foreignKeys) {
        TableRelationGraph graph = new TableRelationGraph();
        for (String tableName : allTables) {
            graph.getNodes().add(new TableRelationGraphNode(tableName));
        }
        for (ForeignKey foreignKey : foreignKeys) {
            TableRelationGraphNode sourceNode = graph.findNodeByTableName(foreignKey.getSourceTableName());
            TableRelationGraphNode targetNode = graph.findNodeByTableName(foreignKey.getTargetTableName());
            sourceNode.getChildren().add(new RelationMapElement(
                    foreignKey.getSourceColumnName(),
                    targetNode,
                    DiscreteDistributionFactory.createDiscreteDistribution(DiscreteDistributionType.valueOf(foreignKey.getSourceDistributionType()), foreignKey.getSourceDistributionParams())));
            targetNode.getParents().add(new RelationMapElement(
                    foreignKey.getSourceColumnName(),
                    sourceNode,
                    DiscreteDistributionFactory.createDiscreteDistribution(DiscreteDistributionType.valueOf(foreignKey.getSourceDistributionType()), foreignKey.getSourceDistributionParams())));
        }
        return graph;
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
        if (column.getType() == ValueType.OBJECT_ID) {
            query.append(column.getName());
            query.append(' ');
            query.append("VARCHAR");
        } else if (column.getType() == ValueType.INT_INTERVAL) {
            query.append(getIntervalCreationQuery(column.getName(), ValueType.INTEGER));
        } else if (column.getType() == ValueType.DATE_INTERVAL) {
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

    private void fillFirstTable(TableRelationGraphNode graphNode,
                                ThreadPoolExecutor executor,
                                QueryTool queryTool,
                                TableGenerator tableGenerator,
                                int count) throws SQLException {
        List<String> generatedKeys = IntStream.range(0, count).mapToObj(i -> tableGenerator.getPrimaryKeyGenerator().nextValue()).toList();
        for (RelationMapElement element : graphNode.getParents()) {
            String fkName = element.getForeignKeyName();
            TableRelationGraphNode node = element.getNode();
            executor.execute(() -> {
                try {
                    fillParentTable(node,
                            executor,
                            queryTool,
                            tableGenerators.stream().filter(tg -> tg.getTableName().equals(node.getTableName())).findFirst().get(),
                            tableGenerator.getTableName(),
                            generatedKeys,
                            fkName,
                            element.getDistribution());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        QueryExecutor queryExecutor = queryTool.getQueryExecutor();
        for (int i = 0; i < count; i++) {
            List<String> columnNames = new ArrayList<>();
            List<String> values = new ArrayList<>();
            columnNames.add(tableGenerator.getPrimaryKeyGenerator().getColumnName());
            values.add(generatedKeys.get(i));
            for (ColumnGenerator columnGenerator : tableGenerator.getColumnGenerators()) {
                columnNames.addAll(Arrays.stream(columnGenerator.getColumnNames()).toList());
                values.addAll(Arrays.stream(columnGenerator.getNextValues()).toList());

            }
            queryExecutor.executeQuery(createInsertQuery(tableGenerator.getTableName(), columnNames, values));
        }
        for (RelationMapElement element : graphNode.getChildren()) {
            TableRelationGraphNode node = element.getNode();
            fillChildTable(element.getNode(),
                    executor,
                    queryTool,
                    tableGenerators.stream().filter(tg -> tg.getTableName().equals(node.getTableName())).findFirst().get(),
                    tableGenerator.getTableName(),
                    generatedKeys,
                    tableGenerator.getPrimaryKeyGenerator().getColumnName(),
                    element.getForeignKeyName(),
                    element.getDistribution());
        }
    }

    private void fillChildTable(TableRelationGraphNode graphNode,
                                ThreadPoolExecutor executor,
                                QueryTool queryTool,
                                TableGenerator tableGenerator,
                                String parentTableName,
                                List<String> parentPrimaryKeyValues,
                                String parentPrimaryKeyName,
                                String parentForeignKeyName,
                                DiscreteDistribution distribution) throws SQLException {
        List<String> generatedKeys = new ArrayList<>();
        List<Integer> keysMap = new ArrayList<>();
        int pkLeft = parentPrimaryKeyValues.size();
        while (pkLeft > 0) {
            int count = distribution.next();
            pkLeft -= count;
            keysMap.add(count);
            generatedKeys.add(tableGenerator.getPrimaryKeyGenerator().nextValue());
        }
        keysMap.removeLast();
        generatedKeys.removeLast();
        for (RelationMapElement element : graphNode.getParents()) {
            String fkName = element.getForeignKeyName();
            TableRelationGraphNode node = element.getNode();
            if (node.getTableName().equals(parentTableName)) continue;
            executor.execute(() -> {
                try {
                    fillParentTable(node,
                            executor,
                            queryTool,
                            tableGenerators.stream().filter(tg -> tg.getTableName().equals(node.getTableName())).findFirst().get(),
                            tableGenerator.getTableName(),
                            generatedKeys,
                            fkName,
                            element.getDistribution());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        int updatedRows = 0;
        QueryExecutor queryExecutor = queryTool.getQueryExecutor();
        for (int i = 0; i < keysMap.size(); i++) {
            List<String> columnNames = new ArrayList<>();
            List<String> values = new ArrayList<>();
            columnNames.add(tableGenerator.getPrimaryKeyGenerator().getColumnName());
            values.add(generatedKeys.get(i));
            for (ColumnGenerator columnGenerator : tableGenerator.getColumnGenerators()) {
                columnNames.addAll(Arrays.stream(columnGenerator.getColumnNames()).toList());
                values.addAll(Arrays.stream(columnGenerator.getNextValues()).toList());
            }
            queryExecutor.executeQuery(createInsertQuery(tableGenerator.getTableName(), columnNames, values));
            for (int j = updatedRows; j < keysMap.get(i); j++) {
                queryExecutor.executeQuery(createUpdateQuery(parentTableName, parentPrimaryKeyName, parentPrimaryKeyValues.get(j), parentForeignKeyName, generatedKeys.get(i)));
            }
        }
        for (RelationMapElement element : graphNode.getChildren()) {
            TableRelationGraphNode node = element.getNode();
            fillChildTable(element.getNode(),
                    executor,
                    queryTool,
                    tableGenerators.stream().filter(tg -> tg.getTableName().equals(node.getTableName())).findFirst().get(),
                    tableGenerator.getTableName(),
                    generatedKeys,
                    tableGenerator.getPrimaryKeyGenerator().getColumnName(),
                    element.getForeignKeyName(),
                    element.getDistribution());
        }
    }

    private void fillParentTable(TableRelationGraphNode graphNode,
                                 ThreadPoolExecutor executor,
                                 QueryTool queryTool,
                                 TableGenerator tableGenerator,
                                 String childTableName,
                                 List<String> childPrimaryKeyValues,
                                 String childForeignKeyName,
                                 DiscreteDistribution distribution) throws SQLException {
        List<String> generatedKeys = new ArrayList<>();
        List<Integer> keysMap = new ArrayList<>();
        for (int j = 0; j < childPrimaryKeyValues.size(); j++) {
            int count = distribution.next();
            for (int i = 0; i < count; i++) {
                generatedKeys.add(tableGenerator.getPrimaryKeyGenerator().nextValue());
            }
            keysMap.add(count);
        }
        for (RelationMapElement element : graphNode.getParents()) {
            String fkName = element.getForeignKeyName();
            TableRelationGraphNode node = element.getNode();
            executor.execute(() -> {
                try {
                    fillParentTable(node,
                            executor,
                            queryTool,
                            tableGenerators.stream().filter(tg -> tg.getTableName().equals(node.getTableName())).findFirst().get(),
                            tableGenerator.getTableName(),
                            generatedKeys,
                            fkName,
                            element.getDistribution());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        QueryExecutor queryExecutor = queryTool.getQueryExecutor();
        int sum = 0;
        for (int countNumber = 0; countNumber < keysMap.size(); countNumber++) {
            int count = keysMap.get(countNumber);

            for (int i = sum; i < sum + count; i++) {
                List<String> columnNames = new ArrayList<>();
                List<String> values = new ArrayList<>();
                columnNames.add(tableGenerator.getPrimaryKeyGenerator().getColumnName());
                columnNames.add(childForeignKeyName);
                values.add(generatedKeys.get(i));
                values.add(childPrimaryKeyValues.get(countNumber));
                for (ColumnGenerator columnGenerator : tableGenerator.getColumnGenerators()) {
                    columnNames.addAll(Arrays.stream(columnGenerator.getColumnNames()).toList());
                    values.addAll(Arrays.stream(columnGenerator.getNextValues()).toList());
                }
                queryExecutor.executeQuery(createInsertQuery(tableGenerator.getTableName(), columnNames, values));
            }
            sum += count;
        }
        for (RelationMapElement element : graphNode.getChildren()) {
            TableRelationGraphNode node = element.getNode();
            if (node.getTableName().equals(childTableName)) continue;
            fillChildTable(element.getNode(),
                    executor,
                    queryTool,
                    tableGenerators.stream().filter(tg -> tg.getTableName().equals(node.getTableName())).findFirst().get(),
                    tableGenerator.getTableName(),
                    generatedKeys,
                    tableGenerator.getPrimaryKeyGenerator().getColumnName(),
                    element.getForeignKeyName(),
                    element.getDistribution());
        }
    }

    private String createInsertQuery(String tableName, List<String> columnNames, List<String> values) {
        StringBuilder query = new StringBuilder("INSERT INTO ");
        query.append(tableName);
        query.append(" (");
        query.append(String.join(",", columnNames));
        query.append(") VALUES (");
        query.append(String.join(", ", values));
        query.append(");");
        return query.toString();
    }

    private String createUpdateQuery(String tableName, String primaryKeyName, String primaryKeyValue, String updateColumnName, String newValue) {
        String query = "UPDATE " + tableName +
                " SET " +
                updateColumnName +
                " = " +
                newValue +
                " WHERE " +
                primaryKeyName +
                " = " +
                primaryKeyValue;
        return query;
    }

}
