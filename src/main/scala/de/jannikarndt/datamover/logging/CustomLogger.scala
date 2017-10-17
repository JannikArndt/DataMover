package de.jannikarndt.datamover.logging

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

import com.typesafe.scalalogging.Logger
import de.jannikarndt.datamover.governance.GovernedID
import org.apache.log4j.lf5.LogLevel
import org.slf4j.LoggerFactory

import scala.collection.mutable
import scala.compat.Platform.EOL

case class LogMessage(level: LogLevel, dateTime: LocalDateTime, message: String) {
    override def toString: String = s"${dateTime.toString} - ${level.getLabel.toUpperCase.padTo(5, ' ')}: $message"
}

class CustomLogger(val name: String) {
    val started: LocalDateTime = LocalDateTime.now
    protected val logger: Logger = Logger(LoggerFactory.getLogger(name))

    val logMessages: mutable.MutableList[LogMessage] = mutable.MutableList[LogMessage]()

    var id: GovernedID = _

    CustomLogger.allLoggers += this

    def error(message: String): Unit = {
        logMessages += LogMessage(LogLevel.ERROR, LocalDateTime.now, message)
        logger.error(message)
    }

    def warn(message: String): Unit = {
        logMessages += LogMessage(LogLevel.WARN, LocalDateTime.now, message)
        logger.warn(message)
    }

    def info(message: String): Unit = {
        logMessages += LogMessage(LogLevel.INFO, LocalDateTime.now, message)
        logger.info(message)
    }

    def debug(message: String): Unit = {
        logMessages += LogMessage(LogLevel.DEBUG, LocalDateTime.now, message)
        logger.debug(message)
    }

    def startedFormatted: String = started.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
}

object CustomLogger {
    var allLoggers: mutable.MutableList[CustomLogger] = mutable.MutableList[CustomLogger]()

    protected val logger: Logger = Logger(LoggerFactory.getLogger(getClass.getName))

    def getLoggingGroups: Seq[Seq[CustomLogger]] = {

        val (result1, oldest) = getLoggingIntervals(allLoggers)
        val (result2, secondButOldest) = getLoggingIntervals(oldest)
        val (result3, thirdButOldest) = getLoggingIntervals(secondButOldest)
        val (result4, fourthButOldest) = getLoggingIntervals(thirdButOldest)

        val seq = Seq(fourthButOldest, result4, result3, result2, result1)
        logger.info(seq.mkString(EOL))
        seq.filterNot(_.isEmpty)
    }

    def getLoggingIntervals(loggers: Seq[CustomLogger]): (Seq[CustomLogger], Seq[CustomLogger]) = {
        if (loggers.isEmpty) {
            logger.info(s"Loggers length: 0. Returning")
            return (Seq.empty, Seq.empty)
        }

        val firstPivotPoint = (loggers.length * 0.5).toInt
        val firstPivotElement = loggers.slice(firstPivotPoint, firstPivotPoint + 1).head

        logger.info(s"Loggers length: ${loggers.length}")
        logger.info(s"First point: $firstPivotPoint")
        logger.info(s"First pivot: ${firstPivotElement.startedFormatted}")
        logger.info(s"loggers.last.started: ${loggers.last.startedFormatted}")
        logger.info(s"firstPivotElement.started: ${firstPivotElement.startedFormatted}")

        firstPivotElement.started.until(loggers.last.started, ChronoUnit.SECONDS) match {
            case weeks if weeks > ChronoUnit.WEEKS.getDuration.getSeconds => splitLoggers(loggers, ChronoUnit.WEEKS)
            case days if days > ChronoUnit.DAYS.getDuration.getSeconds => splitLoggers(loggers, ChronoUnit.DAYS)
            case hours if hours > ChronoUnit.HOURS.getDuration.getSeconds => splitLoggers(loggers, ChronoUnit.HOURS)
            case minutes if minutes > ChronoUnit.MINUTES.getDuration.getSeconds => splitLoggers(loggers, ChronoUnit.MINUTES)
            case seconds if seconds > ChronoUnit.SECONDS.getDuration.getSeconds => splitLoggers(loggers, ChronoUnit.SECONDS)
            case _ => logger.info(s"Not split"); (loggers, Seq.empty)
        }
    }

    def diffIsMoreThenOne(chronoUnit: ChronoUnit, localDateTime: LocalDateTime, other: LocalDateTime): Boolean =
        localDateTime.until(other, chronoUnit) > 1

    def splitLoggers(loggers: Seq[CustomLogger], by: ChronoUnit): (Seq[CustomLogger], Seq[CustomLogger]) = {
        logger.info(s"Split by ${by.toString}")
        val splitPoint: LocalDateTime = LocalDateTime.now().minus(1, by).truncatedTo(by)
        logger.info(s"splitPoint = $splitPoint")
        val beforeSplit = loggers.filter(_.started.isBefore(splitPoint))
        val afterSplit = loggers.filter(_.started.isAfter(splitPoint)) ++ loggers.filter(_.started.isEqual(splitPoint))
        (beforeSplit, afterSplit)
    }
}