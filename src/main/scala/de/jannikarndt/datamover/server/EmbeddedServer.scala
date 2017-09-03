package de.jannikarndt.datamover.server

import com.typesafe.scalalogging.Logger
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.{ServletContextHandler, ServletHolder}
import org.slf4j.LoggerFactory

class EmbeddedServer(val port: Int = 8080, val contextPath: String = "/") {

    protected val logger: Logger = Logger(LoggerFactory.getLogger(getClass.getName))

    val context = new ServletContextHandler(ServletContextHandler.SESSIONS)
    context.setContextPath(contextPath)
    val server = new Server(port)
    server.setHandler(context)
    context.addServlet(new ServletHolder(new ResourceServlet), "*.js")
    context.addServlet(new ServletHolder(new ResourceServlet), "*.css")
    context.addServlet(new ServletHolder(new HtmlServlet), "/")

    def start(): Unit = server.start()

    def stop(): Unit = server.stop()
}