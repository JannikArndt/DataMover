package examples

import java.sql.Timestamp

import de.jannikarndt.datamover._

import scala.concurrent.duration._
import scala.language.postfixOps

object Example {
    val job = new ExampleJob
    job runEvery (10 minutes)
}

class ExampleJob extends Job {
    val source1 = Database.Oracle("connectionString")
    val source2 = File.CSV("path/to/file.csv")
    val target1 = Database.Postgres("connectionString")

    val sports: Table[SportsTableEntry] = source1.GetTable[SportsTableEntry]("SPORTS")
    val hobbies: Table[HobbyTableEntry] = source2.GetTable[HobbyTableEntry]()
    val ballsports: Table[BallSportTableEntry] = target1.GetTable[BallSportTableEntry]("BALLSPORTS")

    val newSports: Table[SportsTableEntry] = sports.getDeltaByTimestamp[BallSportTableEntry](ballsports, _.createTs, _.createTs)
    val filteredSports: Table[SportsTableEntry] = newSports.filter(_.withBall)

    val newHobbies: Table[HobbyTableEntry] = hobbies.getDeltaByID[BallSportTableEntry](ballsports, _.id, _.id)
    val filteredHobbies: Table[HobbyTableEntry] = newHobbies.filter(_.fun > 4)

//    val funWithBalls: Table[IntermediateBallSportTableEntry] = filteredSports.joinHobbies(filteredHobbies)

//    funWithBalls.extractDimension[ActivityTableEntry](_.activityType, _.activityID)

//    ballsports.InsertOrUpdate(funWithBalls.asInstanceOf[Table[BallSportTableEntry]])

    logger.info("Success!")

    override def run(): Unit = {

    }

    override val jobName: String = "Example"
}

class SportsTableEntry extends TableEntry {
    val name: String = ""
    val withBall: Boolean = true
    val createTs: Timestamp = Timestamp.valueOf("2017/01/02")
    val activityType: String = "Outdoor"
}

class HobbyTableEntry extends TableEntry {
    val id: Int = 0
    val name: String = ""
    val fun: Int = 0
}

class BallSportTableEntry extends TableEntry {
    val id: Int = 0
    val name: String = ""
    val withBall: Boolean = true
    val fun: Int = 0
    val activityID = 4
    val createTs: Timestamp = Timestamp.valueOf("2017/01/02")
}

class IntermediateBallSportTableEntry extends BallSportTableEntry {

    val activityType: String = "Outdoor"
}

class ActivityTableEntry extends TableEntry {
    val activityId = 0
    val activityType = "Outdoor"
}