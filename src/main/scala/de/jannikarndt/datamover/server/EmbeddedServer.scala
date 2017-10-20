package de.jannikarndt.datamover.server

import java.io.IOException
import java.net.ServerSocket

import com.typesafe.scalalogging.Logger
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.{ServletContextHandler, ServletHolder}
import org.slf4j.LoggerFactory

class EmbeddedServer() {

    protected val logger: Logger = Logger(LoggerFactory.getLogger(getClass.getName))

    val port: Int = findFreePort()
    val context = new ServletContextHandler(ServletContextHandler.SESSIONS)
    context.setContextPath("/")

    private val server = new Server(port)
    server.setHandler(context)
    context.addServlet(new ServletHolder(new ResourceServlet), "*.js")
    context.addServlet(new ServletHolder(new ResourceServlet), "*.css")
    context.addServlet(new ServletHolder(new HtmlServlet), "/")

    def start(): Unit = server.start()

    def stop(): Unit = server.stop()

    private def findFreePort(): Int = {
        for (i <- 55555 to 55655)
            if (portIsAvailable(i))
                return i
        0
    }

    private def portIsAvailable(port: Int): Boolean =
        try {
            new ServerSocket(port).close()
            true
        } catch {
            case _: IOException => false
        }

}