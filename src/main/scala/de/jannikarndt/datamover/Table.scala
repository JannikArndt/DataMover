package de.jannikarndt.datamover

import java.sql.Timestamp


class Table[entryType <: TableEntry] {
    def getDeltaByTimestamp[otherTableEntryType <: TableEntry]
    (otherTable: Table[otherTableEntryType], tableToTimestamp: entryType => Timestamp, otherTableToTimestamp: otherTableEntryType => Timestamp): Table[entryType] = ???

    def getDeltaByID[otherTableEntryType <: TableEntry]
    (otherTable: Table[otherTableEntryType], tableToId: entryType => Int, otherTableToId: otherTableEntryType => Int): Table[entryType] = ???

    def extractDimension[dimensionTableEntryType <: TableEntry](text: entryType => String, id: entryType => Int): Table[entryType] = ???

    def InsertOrUpdate(table: Table[entryType]): Nothing = ???

    def filter(function: entryType => Boolean): Table[entryType] = ???

    // def join[otherType: TableType[Unit]](otherTable: otherType, on: (this.type , otherType) => Boolean) = ???
}
