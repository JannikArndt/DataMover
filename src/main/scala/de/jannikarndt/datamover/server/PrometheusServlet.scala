package de.jannikarndt.datamover.server

import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

import com.typesafe.scalalogging.Logger
import de.jannikarndt.datamover.monitor.Monitor
import org.slf4j.LoggerFactory

class PrometheusServlet extends HttpServlet {
    protected val logger: Logger = Logger(LoggerFactory.getLogger(getClass.getName))

    override def doGet(request: HttpServletRequest, response: HttpServletResponse): Unit = {
        logger.debug(s"Requesting ${request.getRequestURI}")

        val resp =
            s"# HELP DataMover Gauge for examples.ExampleJob \n" +
                s"# TYPE DataMover gauge\n" +
                s"Input ${Monitor.monitors.lastOption.map(_._2.lastIn).getOrElse("")}\n" +
                s"Output ${Monitor.monitors.lastOption.map(_._2.lastOut).getOrElse("")}"

        response.setContentType("text/plain")
        response.getWriter.write(resp)
    }
}
