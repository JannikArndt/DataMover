package de.jannikarndt.datamover

class PostgresDB(connectionString: String) {
    def GetTable[tableEntryType <: TableEntry](tableName: String): Table[tableEntryType] = new Table[tableEntryType]
}
