package de.jannikarndt.datamover

import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import scala.concurrent.duration.Duration

trait Job {

    protected val logger: Logger = Logger(LoggerFactory.getLogger(getClass.getName))

    def run(): Unit

    val jobName: String

    def runEvery(interval: Duration): Unit = {
        // scheduler.schedule
    }

    def initialLog(): Unit = {
        logger.info(s"Starting job $jobName")
    }

    def main(args: Array[String]): Unit = {
        initialLog()
        run()
    }
}