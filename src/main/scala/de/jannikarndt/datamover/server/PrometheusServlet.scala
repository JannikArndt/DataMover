package de.jannikarndt.datamover.server

import java.net.URLDecoder
import java.util
import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

import com.typesafe.scalalogging.Logger
import io.prometheus.client.CollectorRegistry
import org.slf4j.LoggerFactory

class PrometheusServlet extends HttpServlet {
    protected val logger: Logger = Logger(LoggerFactory.getLogger(getClass.getName))

    override def doGet(request: HttpServletRequest, response: HttpServletResponse): Unit = {
        logger.debug(s"Requesting ${request.getRequestURI}")

        io.prometheus.client.exporter.common.TextFormat.write004(
            response.getWriter,
            CollectorRegistry.defaultRegistry.filteredMetricFamilySamples(parseQuery(request.getRequestURI))
        )
    }

    // see https://github.com/prometheus/client_java/blob/master/simpleclient_httpserver/src/main/java/io/prometheus/client/exporter/HTTPServer.java#L100
    protected def parseQuery(query: String): util.Set[String] = {
        val names: util.Set[String] = new util.HashSet[String]
        if (query != null) {
            val pairs: Array[String] = query.split("&")
            for (pair <- pairs) {
                val idx: Int = pair.indexOf("=")
                if (idx != -1 && URLDecoder.decode(pair.substring(0, idx), "UTF-8") == "name[]") names.add(URLDecoder.decode(pair.substring(idx + 1), "UTF-8"))
            }
        }
        names
    }
}
