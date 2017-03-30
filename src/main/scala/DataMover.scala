import java.util.logging.{Level, Logger}

import scala.concurrent.duration.Duration

class Job {
    private val logger = new Logger()

    def runEvery(interval: Duration): Unit = {
        // scheduler.schedule
    }

    def logTo(filename: String): Unit = {
        // configure logging
    }

    protected def log(msg: String) = {
        logger.log(Level.INFO, msg)
    }
}

abstract class DataAdapter {

}

object File {
    def CSV(filepath: String) = new CsvFile(filepath)
}

abstract class File[FileType] extends DataAdapter {
    abstract def GetTable[tableType <: Table[FileType]](tableName: String): Table[FileType]
}

abstract class Database[DbType] extends DataAdapter{
    abstract def GetTable[tableType <: Table[DbType]](tableName: String): Table[DbType]
}

object Database  {
    def Postgres(connectionString: String) = new PostgresDB(connectionString)

    def Oracle(connectionString: String) = new OracleDB(connectionString)
}



abstract class TableType[sourceType]
class OracleTable extends TableType[OracleDB]
class PostgresTable extends TableType[PostgresDB]
class CsvTable extends TableType[CsvFile]

class OracleDB(connectionString: String) extends Database[OracleTable] {
    override def GetTable[tableType <: Table[OracleTable]](tableName: String): Table[tableType] = new Table[tableType]
}

class PostgresDB(connectionString: String) extends Database[PostgresTable] {
    override def GetTable[tableType <: Table[PostgresTable]](tableName: String): Table[tableType] = new Table[tableType]
}

class CsvFile(filepath: String) extends File {
    override def GetTable[tableType <: Table[CsvTable]](tableName: String = ""): Table[tableType] = new Table[tableType]
}

class Table[tableType <: Database[tableType]] {
    def InsertOrUpdate(table: tableType): Nothing = ???

    def filter(function: tableType => Boolean): tableType = ???
    // def join[otherType: TableType[Unit]](otherTable: otherType, on: (this.type , otherType) => Boolean) = ???
}
