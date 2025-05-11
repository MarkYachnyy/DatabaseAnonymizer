package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.graph;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RelationMapElement {
    private String foreignKeyName;
    private TableRelationGraphNode node;
}
