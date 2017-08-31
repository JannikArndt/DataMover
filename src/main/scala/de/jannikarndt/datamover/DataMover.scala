package de.jannikarndt.datamover

import java.time.{LocalDateTime, ZoneOffset}
import java.util.{Collections, Date}

import com.typesafe.scalalogging.{Logger, StrictLogging}
import org.quartz.impl.StdSchedulerFactory
import org.quartz.{JobBuilder, JobExecutionContext, SimpleScheduleBuilder, TriggerBuilder}
import org.slf4j.LoggerFactory

import scala.collection.mutable
import scala.concurrent.duration.Duration

object DataMover {
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

abstract class DataMover(jobName: String) extends org.quartz.Job with StrictLogging with Monitoring {
    logger.info(s"Starting job $jobName")

    def run(): Unit

    override def execute(jobExecutionContext: JobExecutionContext): Unit = run()

    def dumpMonitor() = logger.info(monitor.dump())
}

trait Monitoring{
    protected val monitor: Monitor = Monitor.getMonitor(getClass.getName)
}

class Monitor(name: String){
    private var inputLong = mutable.Map(LocalDateTime.now() -> 0L)
    private var inputString = mutable.Map(LocalDateTime.now() -> "")

    private var outputLong = mutable.Map(LocalDateTime.now() -> 0L)
    private var outputString = mutable.Map(LocalDateTime.now() -> "")

    def input(number: Long): Unit = inputLong += (LocalDateTime.now() -> number)
    def input(text: String): Unit = inputString += (LocalDateTime.now() -> text)

    def output(number: Long): Unit = outputLong += (LocalDateTime.now() -> number)
    def output(text: String): Unit = outputString += (LocalDateTime.now() -> text)

    def dump(): String = {
        implicit val localDateOrdering: Ordering[LocalDateTime] = Ordering.by(_.toEpochSecond(ZoneOffset.UTC))
        def makeString(map: collection.Map[_ <: LocalDateTime, _ <: Any]) = map.toList.sortBy(_._1).map(_.productIterator.mkString("\t")).mkString("\n")

        val in = makeString(inputLong.mapValues(x => x.toString) ++ inputString)
        val out = makeString(outputLong.mapValues(x => x.toString) ++ outputString)
        s"Input: \n$in\nOutput: \n$out"
    }
}

object Monitor {
    private val monitors: mutable.Map[String, Monitor] = mutable.Map[String, Monitor]()

    def getMonitor(className: String): Monitor = {
        monitors.getOrElseUpdate(className, new Monitor(className))
    }
}