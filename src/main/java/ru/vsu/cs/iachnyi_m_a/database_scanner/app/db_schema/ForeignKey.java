package ru.vsu.cs.iachnyi_m_a.database_scanner.app.db_schema;

public class ForeignKey {
    private final String refersToTable;
    private final String refersToColumn;

    public ForeignKey(String refersToTable, String refersToColumn) {
        this.refersToTable = refersToTable;
        this.refersToColumn = refersToColumn;
    }

    public String refersToTable() {
        return refersToTable;
    }

    public String refersToColumn() {
        return refersToColumn;
    }
}
