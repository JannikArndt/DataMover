package de.jannikarndt.datamover

import java.util.Date

import com.typesafe.scalalogging.Logger
import de.jannikarndt.datamover.monitor.Monitoring
import org.quartz.impl.StdSchedulerFactory
import org.quartz.{JobBuilder, JobExecutionContext, SimpleScheduleBuilder, TriggerBuilder}
import org.slf4j.LoggerFactory

import scala.collection.mutable
import scala.concurrent.duration.Duration

object DataMover {
    var loggers: mutable.MutableList[customLogger] = mutable.MutableList[customLogger]()

    def run(jobClass: Class[_ <: DataMover]): JobWithClass = {
        new JobWithClass(jobClass)
    }
}

class JobWithClass(jobClass: Class[_ <: DataMover]) {
    def every(duration: Duration): Date = {
        val jobName = jobClass.getName
        val groupName = "DataMover"
        val interval = duration.toMillis

        val trigger = TriggerBuilder.newTrigger.withIdentity(jobName, groupName)
            .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(interval).repeatForever()).build

        val job = JobBuilder.newJob(jobClass).withIdentity(jobName, groupName).build

        val scheduler = new StdSchedulerFactory().getScheduler
        scheduler.start()
        scheduler.scheduleJob(job, trigger)
    }
}

abstract class DataMover(jobName: String) extends org.quartz.Job with Monitoring {
//    protected val logger: Logger = Logger(LoggerFactory.getLogger(getClass.getName))
    protected val logger = new customLogger(getClass.getName)

    logger.info(s"Starting job $jobName")

    def run(): Unit

    override def execute(jobExecutionContext: JobExecutionContext): Unit = run()
}

class customLogger(name: String){
    protected var logger: Logger = Logger(LoggerFactory.getLogger(name))

    val logMessages: mutable.MutableList[String] = mutable.MutableList[String]()

    DataMover.loggers += this

    def info(message: String): Unit = {
        logMessages += message
        logger.info(message)
    }

}
