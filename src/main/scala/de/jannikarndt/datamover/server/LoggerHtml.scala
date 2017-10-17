package de.jannikarndt.datamover.server

import de.jannikarndt.datamover.logging.CustomLogger

import scala.compat.Platform.EOL

object LoggerHtml {

    def getLoggerHtml: String = {
        """<div class="bd-example bd-example-tabs"><div class="row"><div class="col-3" style="height:300px; overflow:scroll">""" +
            """<div class="nav flex-column nav-pills" id="v-pills-tab" role="tablist">""" +
            CustomLogger.allLoggers.take(1).map(logger => s"""<a class="nav-link active" id="${toLink(logger.started.toString)}-tab" data-toggle="pill" href="#${toLink(logger.started.toString)}" role="tab">${logger.startedFormatted}</a>""").mkString(EOL) +
            CustomLogger.allLoggers.drop(1).map(logger => s"""<a class="nav-link"        id="${toLink(logger.started.toString)}-tab" data-toggle="pill" href="#${toLink(logger.started.toString)}" role="tab">${logger.startedFormatted}</a>""").mkString(EOL) +
            """</div></div>""" +
            """<div class="col-9" style="height:300px; overflow:scroll; background-color:#ddd"><div class="tab-content" id="v-pills-tabContent">""" +
            CustomLogger.allLoggers.take(1).map(logger => getLoggerTabItem(logger, true)).mkString(EOL) +
            CustomLogger.allLoggers.drop(1).map(logger => getLoggerTabItem(logger)).mkString(EOL) +
            """</div></div></div></div>"""
    }

    def getLoggerTabItem(logger: CustomLogger, active: Boolean = false): String = {
        s"""<div class="tab-pane${if (active) " show active" else ""}" id="${toLink(logger.started.toString)}" role="tabpanel">""" +
            s"""ID <i>${logger.id.identifier.toString}</i>""" +
            s"""<pre>${logger.logMessages.mkString(EOL)}</pre></div>"""
    }

    def toLink(text: String): String = "tab" + text.toLowerCase.replaceAll("""[^a-zA-Z\d]""", "")
}
