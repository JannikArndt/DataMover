package de.jannikarndt.datamover.monitor

import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

import com.typesafe.scalalogging.Logger
import de.jannikarndt.datamover.DataMover
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.{ServletContextHandler, ServletHolder}
import org.slf4j.LoggerFactory
import scala.compat.Platform.EOL

class EmbeddedServlet(val port: Int = 8080, val contextPath: String = "/") {

    val context = new ServletContextHandler(ServletContextHandler.SESSIONS)
    context.setContextPath(contextPath)
    val server = new Server(port)
    server.setHandler(context)
    context.addServlet(new ServletHolder(new MonitoringServlet), "/*")

    def start(): Unit = server.start()

    def stop(): Unit = server.stop()
}

class MonitoringServlet extends HttpServlet {
    protected val logger: Logger = Logger(LoggerFactory.getLogger(getClass.getName))

    override def doGet(request: HttpServletRequest, response: HttpServletResponse): Unit = {
        response.setContentType("text/html")
        response.getWriter.write(getHtml(request))
    }

    def getHtml(request: HttpServletRequest): String =
        request.getRequestURI match {
            case "/" => sites.index()
            case other => s"<h1>Error 404</h1>Route $other not found."
        }

}

object sites {
    def index(): String = {
        s"<h1>Monitoring</h1>" +
            Monitor.monitors.map(monitor => s"<h2>${monitor._1}</h2><pre>${monitor._2.dump()}</pre>").mkString(EOL) +
            DataMover.loggers.map(logger => s"<h2>Log for ${logger.name}</h2>\n" +
                s"<h4>${logger.started}</h4>" +
                s"<pre>${logger.logMessages.mkString("\n")}</pre>").mkString(EOL)
    }
}