package de.jannikarndt.datamover.server

import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import scala.compat.Platform.EOL

object Sites {
    protected val logger: Logger = Logger(LoggerFactory.getLogger(getClass.getName))

    def index: String =
        s"""$getHeader
           |<div class="container">
           |<h1>Monitoring</h1>
           |${MonitorHtml.getMonitors}
           |$EOL
           |$EOL
           |<h1>Loggers</h1>${LoggerHtml.getLoggerHtml}
           |</div>""".stripMargin

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
}
