import scala.concurrent.duration._
import scala.language.postfixOps

object Example {
    val job = new ExampleJob
    job runEvery (10 minutes)
    job logTo "mylogfile.log"
}

class ExampleJob extends Job {
    val source1 = Database.Oracle("connectionString")
    val source2 = File.CSV("path/to/file.csv")
    val target1 = Database.Postgres("connectionString")

    val sports = source1.GetTable[SportsTable]("SPORTS")
    val hobbies = source2.GetTable[HobbyTable]()
    val ballsports = target1.GetTable[BallSportTable]("BALLSPORTS")

    val filteredSports = sports.filter(_.withBall)
    val filteredHobbies = hobbies.filter(_.fun > 4)

    val funWithBalls = filteredSports.joinHobbies(filteredHobbies)

    ballsports.InsertOrUpdate(funWithBalls)

    log("Success!")
}

class SportsTable(val tableName: String) extends Table[OracleTable] {
    val name: String = ""
    val withBall: Boolean = true

    def joinHobbies(hobbies: HobbyTable): BallSportTable = {
        new BallSportTable
    }
}

class HobbyTable extends Table[CsvTable] {
    val name: String = ""
    val fun: Int = 0
}

class BallSportTable extends Table[PostgresTable] {
    val name: String = ""
    val withBall: Boolean = true
    val fun: Int = 0
}