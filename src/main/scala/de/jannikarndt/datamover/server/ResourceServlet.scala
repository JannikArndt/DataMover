package de.jannikarndt.datamover.server

import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import scala.compat.Platform.EOL
import scala.io.Source

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
