package de.jannikarndt.datamover

class OracleDB(connectionString: String) {
    def GetTable[tableEntryType <: TableEntry](tableName: String): Table[tableEntryType] = new Table[tableEntryType]
}
