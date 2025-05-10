package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.graph;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class TableRelationGraphNode {
    private String tableName;
    private Map<String, TableRelationGraphNode> children;
    private Map<String, TableRelationGraphNode> parents;

    public TableRelationGraphNode(String tableName) {
        this.tableName = tableName;
        this.children = new HashMap<>();
        this.parents = new HashMap<>();
    }
}
