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
           |<h1>Loggers</h1>
           |${LoggerHtml.getLoggerHtml}
           |$EOL
           |$EOL
           |<h1>Governance IDs</h1>
           |${GovernorHtml.getGovernedIds}
           |</div>""".stripMargin

    def getHeader: String =
        """<head>
          |<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css" integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">
          |<link rel="stylesheet" href="server/tether.min.css">
          |<link rel="stylesheet" href="server/style.css">
          |<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
          |<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js" integrity="sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4" crossorigin="anonymous"></script>
          |<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js" integrity="sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1" crossorigin="anonymous"></script>
          |</head>
        """.stripMargin
}
