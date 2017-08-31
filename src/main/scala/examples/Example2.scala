package examples


import de.jannikarndt.datamover.{DataMover, File}

import scala.concurrent.duration._
import scala.language.postfixOps


object ExampleJob {

    def main(args: Array[String]): Unit = DataMover run classOf[Example2] every (10 seconds)

}

class Example2 extends DataMover("Example2") {

    override def run(): Unit = {
        logger.info("In Job")

        val randomCount = scala.util.Random.nextInt(30)

        File.append("output/foo.txt", randomCount.toString)
    }

}