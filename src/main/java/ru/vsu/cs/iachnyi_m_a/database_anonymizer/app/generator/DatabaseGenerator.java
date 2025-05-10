package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator;

import org.postgresql.util.PSQLException;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.base.Column;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.base.DatabaseSchema;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.base.Table;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.base.ValueType;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.constraint.ConstraintSet;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.constraint.ForeignKey;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.constraint.PrimaryKey;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.rule.Rule;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.rule.RuleSet;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.graph.TableRelationGraph;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.graph.TableRelationGraphNode;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.query.QueryExecutor;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.query.QueryTool;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator.ColumnGenerator;

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

    public void fillDatabase(DatabaseSchema schema, ConstraintSet constraintSet, RuleSet ruleSet) throws SQLException {

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

        //

        String firstTableName = "users";
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 2, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        fillFirstTable(tableRelationGraph.findNodeByTableName(firstTableName),
                threadPoolExecutor,
                queryTool,
                tableGenerators.stream().filter(tg -> tg.getTableName().equals(firstTableName)).findFirst().get(),
                10);

//        for (TableGenerator tableGenerator : tableGenerators) {
//            String columns = tableGenerator.getColumnGenerators().
//                    stream().
//                    map(g -> String.join(", ", g.getColumnNames())).
//                    collect(Collectors.joining(", "));
//            QueryExecutor executor = queryTool.getQueryExecutor();
//            for (int i = 0; i < 1000; i++) {
//                StringBuilder query = new StringBuilder("INSERT INTO ");
//                query.append(tableGenerator.getTableName());
//                query.append(" (");
//                query.append(columns);
//                query.append(") VALUES (");
//                query.append(tableGenerator.getColumnGenerators().
//                        stream().
//                        map(g -> {
//                            boolean generate = Math.random() > g.getNullChance();
//                            return generate ? String.join(", ", g.getNextValues()) : IntStream.range(0, g.getColumnNames().length).mapToObj(_ -> "NULL").collect(Collectors.joining(", "));
//                        }).
//                        collect(Collectors.joining(", ")));
//                query.append(");");
//                executor.executeQuery(query.toString());
//            }
//        }
    }

    private TableRelationGraph getTableRelationGraph(List<String> allTables, List<ForeignKey> foreignKeys) {
        TableRelationGraph graph = new TableRelationGraph();
        for (String tableName : allTables) {
            graph.getNodes().add(new TableRelationGraphNode(tableName));
        }
        for (ForeignKey foreignKey : foreignKeys) {
            TableRelationGraphNode sourceNode = graph.findNodeByTableName(foreignKey.getSourceTableName());
            TableRelationGraphNode targetNode = graph.findNodeByTableName(foreignKey.getTargetTableName());
            sourceNode.getChildren().put(foreignKey.getSourceColumnName(), targetNode);
            targetNode.getParents().put(foreignKey.getSourceColumnName(), sourceNode);
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

    private void fillFirstTable(TableRelationGraphNode graphNode, ThreadPoolExecutor executor, QueryTool queryTool, TableGenerator generator, int count) throws SQLException {
        List<String> generatedKeys = IntStream.range(0, count).mapToObj(i -> generator.getPrimaryKeyGenerator().nextValue()).toList();
        //System.out.println(generatedKeys);
        for (String fkName : graphNode.getParents().keySet()) {
            TableRelationGraphNode node = graphNode.getParents().get(fkName);
            executor.execute(() -> {
                try {
                    fillParentTable(node, executor, queryTool, tableGenerators.stream().filter(tg -> tg.getTableName().equals(node.getTableName())).findFirst().get(), generatedKeys, fkName);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        QueryExecutor queryExecutor = queryTool.getQueryExecutor();
        for (int i = 0; i < count; i++) {
            StringBuilder query = new StringBuilder("INSERT INTO ");
            query.append(generator.getTableName());
            query.append(" (");
            query.append(generator.getPrimaryKeyGenerator().getColumnName());
            for (ColumnGenerator columnGenerator : generator.getColumnGenerators()) {
                query.append(", ");
                query.append(String.join(",", columnGenerator.getColumnNames()));
            }
            query.append(") VALUES (");
            query.append(generatedKeys.get(i));
            for (ColumnGenerator columnGenerator : generator.getColumnGenerators()) {
                query.append(", ");
                query.append(String.join(",", columnGenerator.getNextValues()));
            }
            query.append(");");
            queryExecutor.executeQuery(query.toString());
        }
//        for (String fkName : graphNode.getParents().keySet()) {
//            TableRelationGraphNode node = graphNode.getParents().get(fkName);
//            fillChildTable(node, executor, queryTool, tableGenerators.stream().filter(tg -> tg.getTableName().equals(node.getTableName())).findFirst().get(), generatedKeys, fkName);
//        }
    }

    private void fillChildTable(TableRelationGraphNode graphNode, ThreadPoolExecutor executor, QueryTool queryTool, TableGenerator generator, List<String> primaryKeys) {

    }

    private void fillParentTable(TableRelationGraphNode graphNode, ThreadPoolExecutor executor, QueryTool queryTool, TableGenerator generator, List<String> primaryKeys, String foreignKeyName) throws SQLException {
        Random random = new Random();
        List<String> generatedKeys = new ArrayList<>();
        List<Integer> keysMap = new ArrayList<>();
        for (int j = 0; j < primaryKeys.size(); j++) {
            int count = random.nextInt(2, 7);
            for (int i = 0; i < count; i++) {
                generatedKeys.add(generator.getPrimaryKeyGenerator().nextValue());
            }
            keysMap.add(count);
        }
        //System.out.println(generatedKeys);
        for (String fkName : graphNode.getParents().keySet()) {
            TableRelationGraphNode node = graphNode.getParents().get(fkName);
            executor.execute(() -> {
                try {
                    fillParentTable(node, executor, queryTool, tableGenerators.stream().filter(tg -> tg.getTableName().equals(node.getTableName())).findFirst().get(), generatedKeys, fkName);
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
                StringBuilder query = new StringBuilder("INSERT INTO ");
                query.append(generator.getTableName());
                query.append(" (");
                query.append(generator.getPrimaryKeyGenerator().getColumnName());
                for (ColumnGenerator columnGenerator : generator.getColumnGenerators()) {
                    query.append(", ");
                    query.append(String.join(",", columnGenerator.getColumnNames()));
                }
                query.append(", ");
                query.append(foreignKeyName);
                query.append(") VALUES (");
                query.append(generatedKeys.get(i));
                for (ColumnGenerator columnGenerator : generator.getColumnGenerators()) {
                    query.append(", ");
                    query.append(String.join(",", columnGenerator.getNextValues()));
                }
                query.append(", ");
                query.append(primaryKeys.get(countNumber));
                query.append(");");
                queryExecutor.executeQuery(query.toString());
            }
            sum += count;
        }
    }

}
