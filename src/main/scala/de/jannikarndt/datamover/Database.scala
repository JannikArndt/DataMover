package de.jannikarndt.datamover

object Database  {
    def Postgres(connectionString: String) = new PostgresDB(connectionString)

    def Oracle(connectionString: String) = new OracleDB(connectionString)
}
