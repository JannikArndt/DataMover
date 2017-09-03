package de.jannikarndt.datamover

class CsvFile(filepath: String) {
    def GetTable[tableEntryType <: TableEntry](): Table[tableEntryType] = new Table[tableEntryType]
}
