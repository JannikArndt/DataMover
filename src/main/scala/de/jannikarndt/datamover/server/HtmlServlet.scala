package de.jannikarndt.datamover.server

import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

class HtmlServlet extends HttpServlet {
    protected val logger: Logger = Logger(LoggerFactory.getLogger(getClass.getName))

    override def doGet(request: HttpServletRequest, response: HttpServletResponse): Unit = {
        logger.debug(s"Requesting ${request.getRequestURI}")
        response.setContentType("text/html")
        response.getWriter.write(getHtml(request))
    }

    def getHtml(request: HttpServletRequest): String =
        request.getRequestURI match {
            case "/" => Sites.index
            case other => s"<h1>Error 404</h1>Route $other not found."
        }
}
