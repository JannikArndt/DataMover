package de.jannikarndt.datamover.logging

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import com.typesafe.scalalogging.Logger
import de.jannikarndt.datamover.governance.GovernedID
import org.apache.log4j.lf5.LogLevel
import org.slf4j.LoggerFactory

import scala.collection.mutable

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
}