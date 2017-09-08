package examples


import de.jannikarndt.datamover.DataMover
import de.jannikarndt.datamover.io.File

import scala.concurrent.duration._
import scala.language.postfixOps

object ExampleJob {

    def main(args: Array[String]): Unit = DataMover run classOf[ExampleJob] every (10 seconds)

}

class ExampleJob extends DataMover("ExampleJob") {

    override def run(): Unit = {
        logger.info("In Job")

        val randomCount = scala.util.Random.nextInt(30)

        monitor.input(randomCount)

        File.append("output/foo.txt", randomCount.toString)

        monitor.output("Appended successfully")
    }

}