package de.jannikarndt.datamover

import java.time.LocalDateTime
import java.util.Date

import de.jannikarndt.datamover.logging.CustomLogger
import de.jannikarndt.datamover.monitor.Monitoring
import org.quartz.impl.StdSchedulerFactory
import org.quartz.{JobBuilder, JobExecutionContext, SimpleScheduleBuilder, TriggerBuilder}

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

abstract class DataMover(jobName: String) extends org.quartz.Job with Monitoring {
    protected val logger = new CustomLogger(getClass.getName)

    logger.info(s"Starting job $jobName at ${LocalDateTime.now}")

    def run(): Unit

    override def execute(jobExecutionContext: JobExecutionContext): Unit = run()
}