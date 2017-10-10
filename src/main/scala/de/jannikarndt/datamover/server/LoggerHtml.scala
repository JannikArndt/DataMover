package de.jannikarndt.datamover.server

import de.jannikarndt.datamover.logging.CustomLogger

import scala.compat.Platform.EOL

object LoggerHtml {
    def getLoggerHtml: String = {

        val loggingGroups = CustomLogger.getLoggingGroups

        """<div class="bd-example bd-example-tabs" role="tabpanel">""" +
            """<ul class="nav nav-tabs" role="tablist">""" +
            loggingGroups.take(1).map(l => getLoggerMenuItem(l, active = true)).mkString(EOL) +
            loggingGroups.drop(1).map(l => getLoggerMenuItem(l)).mkString(EOL) +
            """</ul><div class="tab-content">""" +
            loggingGroups.take(1).map(l => getLoggerTabItems(l, active = true)).mkString(EOL) +
            loggingGroups.drop(1).map(l => getLoggerTabItems(l)).mkString(EOL) +
            """</div></div>"""
    }

    def getLoggerMenuItem(loggerGroup: Seq[CustomLogger], active: Boolean = false): String =
        s"""
           |<li class="nav-item dropdown">
           |  <a class="nav-link dropdown-toggle${if (active) " active" else ""}" data-toggle="dropdown" href="#"
           |  role="button" aria-haspopup="true" aria-expanded="false">Since ${loggerGroup.headOption.map(_.startedFormatted).getOrElse("")}</a>
           |  <div class="dropdown-menu">""".stripMargin +
            loggerGroup.map(logger =>
                s"""<a class="dropdown-item" data-toggle="tab" href="#${toLink(logger.started.toString)}">${logger.startedFormatted}</a>""").mkString(EOL) +
            s"""</div></li>"""

    def getLoggerTabItems(loggerGroup: Seq[CustomLogger], active: Boolean = false): String = {
        if (active) {
            loggerGroup.take(1).map(logger => getLoggerTabItem(logger, true)).mkString(EOL) +
                loggerGroup.drop(1).map(logger => getLoggerTabItem(logger, false)).mkString(EOL)
        }
        else
            loggerGroup.map(logger => getLoggerTabItem(logger)).mkString(EOL)
    }

    def getLoggerTabItem(logger: CustomLogger, active: Boolean = false): String = {
        s"""<div class="tab-pane${if (active) " active" else ""}" id="${toLink(logger.started.toString)}" role="tabpanel">""" +
            s"""ID <i>${logger.id.identifier.toString}</i>""" +
            s"""<pre>${logger.logMessages.mkString(EOL)}</pre></div>"""
    }

    def toLink(text: String): String = "tab" + text.toLowerCase.replaceAll("""[^a-zA-Z\d]""", "")
}
