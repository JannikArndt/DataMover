package examples


import de.jannikarndt.datamover.DataMover
import de.jannikarndt.datamover.io.File
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.duration._
import scala.io.{BufferedSource, Source}
import scala.language.postfixOps

object ExampleJob {

    // Schedule an ExampleJob to run every 10 seconds
    def main(args: Array[String]): Unit = DataMover run classOf[ExampleJob] every (10 seconds)

}

class ExampleJob extends DataMover("ExampleJob") {

    // `run` is being executed everytime the job is scheduled
    override def run(): Unit = {
        // here you can access
        // - logger => Log debug, info or error information
        // - monitor => track throughput
        // - governedId => append this to your output to find the job that generated it

        logger.info(s"Job with ${governedId.identifier} is starting…")

        val randomUser: BufferedSource = Source.fromURL("https://randomuser.me/api/")
        val user: JsValue = Json.parse(randomUser.mkString)
        val name = s"${user \\ "first" head} ${user \\ "last" head}".replace("\"", "")

        monitor.input(1)
        logger.info(s"Found user $name")

        File.append("output/foo.txt", s"New user: $name (by job $governedId)\n")

        monitor.output(1)
        monitor.output(s"Appended user $name successfully")
    }

}