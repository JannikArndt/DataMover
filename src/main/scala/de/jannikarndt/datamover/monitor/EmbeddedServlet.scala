package de.jannikarndt.datamover.monitor

import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

import de.jannikarndt.datamover.DataMover
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.{ServletContextHandler, ServletHolder}

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
    override def doGet(request: HttpServletRequest, response: HttpServletResponse): Unit = {
        response.setContentType("text/html")

        val out = response.getWriter

        out.println(s"<h1>Monitoring</h1>")

        Monitor.monitors.foreach(monitor => out.println(s"<h2>${monitor._1}</h2><pre>${monitor._2.dump()}</pre>"))

        DataMover.loggers.foreach(logger => out.println(s"<h2>Log</h2>\n<pre>${logger.logMessages.mkString("\n")}</pre>"))
    }
}