package de.jannikarndt.datamover.monitor

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

import com.typesafe.scalalogging.Logger
import de.jannikarndt.datamover.DataMover
import de.jannikarndt.datamover.logging.CustomLogger
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.{ServletContextHandler, ServletHolder}
import org.slf4j.LoggerFactory

import scala.compat.Platform.EOL
import scala.io.Source

class EmbeddedServlet(val port: Int = 8080, val contextPath: String = "/") {

    protected val logger: Logger = Logger(LoggerFactory.getLogger(getClass.getName))

    val context = new ServletContextHandler(ServletContextHandler.SESSIONS)
    context.setContextPath(contextPath)
    val server = new Server(port)
    server.setHandler(context)
    context.addServlet(new ServletHolder(new ResourceServlet), "*.js")
    context.addServlet(new ServletHolder(new ResourceServlet), "*.css")
    context.addServlet(new ServletHolder(new MonitoringServlet), "/")

    def start(): Unit = server.start()

    def stop(): Unit = server.stop()

}

class ResourceServlet extends HttpServlet {
    val classLoader: ClassLoader = this.getClass.getClassLoader
    protected val logger: Logger = Logger(LoggerFactory.getLogger(getClass.getName))

    override def doGet(request: HttpServletRequest, response: HttpServletResponse): Unit = {
        logger.info(s"Requesting ${request.getRequestURI}")
        response.setContentType("text/html")
        response.getWriter.write(getResource(request.getRequestURI.stripPrefix("/")))
    }

    def getResource(path: String): String = Source.fromInputStream(classLoader.getResourceAsStream(path)).getLines().mkString(EOL)
}

class MonitoringServlet extends HttpServlet {
    protected val logger: Logger = Logger(LoggerFactory.getLogger(getClass.getName))

    override def doGet(request: HttpServletRequest, response: HttpServletResponse): Unit = {
        logger.info(s"Requesting ${request.getRequestURI}")
        response.setContentType("text/html")
        response.getWriter.write(getHtml(request))
    }

    def getHtml(request: HttpServletRequest): String =
        request.getRequestURI match {
            case "/" => sites.index
            case other => s"<h1>Error 404</h1>Route $other not found."
        }
}

object sites {
    protected val logger: Logger = Logger(LoggerFactory.getLogger(getClass.getName))

    def index: String = s"""$getHeader <div class="container"><h1>Monitoring</h1>$getMonitors$EOL$EOL<h1>Loggers</h1>$getLoggerHtml</div>"""

    def getListOfFiles(dir: String): List[java.io.File] = {
        val d = new java.io.File(dir)
        if (d.exists && d.isDirectory) {
            d.listFiles.filter(_.isFile).toList
        } else {
            List[java.io.File]()
        }
    }

    def getHeader: String =
        """<head>
          |<link rel="stylesheet" href="server/bootstrap.min.css">
          |<link rel="stylesheet" href="server/tether.min.css">
          |<link rel="stylesheet" href="server/style.css">
          |<script src="server/jquery-3.2.1.min.js"></script>
          |<script src="server/tether.min.js"></script>
          |<script src="server/bootstrap.min.js"></script>
          |</head>
        """.stripMargin

    def getMonitors: String = Monitor.monitors.map(m => s"<h2>Monitor: ${m._1}</h2>" + EOL + getMonitorHtml(m._2)).mkString(EOL)

    def getMonitorHtml(monitor: Monitor): String =
        """<div id="accordion" role="tablist"><div class="card"><div class="card-header" role="tab" id="headingOne"><h5 class="mb-0">""" +
            """<a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">""" +
            "Input" +
            s"""</a></h5></div><div id="collapseOne" class="collapse" role="tabpanel" aria-labelledby="headingOne"><div class="card-block"><pre>""" +
            monitor.dumpIn +
            s"""</pre></div></div></div><div class="card"><div class="card-header" role="tab" id="headingTwo"><h5 class="mb-0">""" +
            s"""<a class="collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo">""" +
            "Output" +
            s"""</a></h5></div><div id="collapseTwo" class="collapse" role="tabpanel" aria-labelledby="headingTwo"><div class="card-block"><pre>""" +
            monitor.dumOut +
            s"""</pre></div></div></div></div>"""

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
            s"""<pre>${logger.logMessages.mkString(EOL)}</pre></div>"""
    }

    def toLink(text: String): String = "tab" + text.toLowerCase.replaceAll("""[^a-zA-Z\d]""", "")
}