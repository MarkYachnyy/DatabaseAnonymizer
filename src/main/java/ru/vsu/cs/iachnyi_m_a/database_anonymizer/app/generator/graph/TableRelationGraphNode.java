package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.graph;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class TableRelationGraphNode {
    private String tableName;
    private List<RelationMapElement> children;
    private List<RelationMapElement> parents;

    public TableRelationGraphNode(String tableName) {
        this.tableName = tableName;
        this.children = new ArrayList<>();
        this.parents = new ArrayList<>();
    }
}
