package de.jannikarndt.datamover.server

import de.jannikarndt.datamover.monitor.Monitor

import scala.compat.Platform.EOL

object MonitorHtml {
    def getMonitors: String = Monitor.monitors.map(m => s"<h2>Monitor: ${m._1}</h2>" + EOL + getMonitorHtml(m._2)).mkString(EOL)

    def getMonitorHtml(monitor: Monitor): String =
        """<div id="accordion" role="tablist"><div class="card"><div class="card-header" role="tab" id="headingOne"><h5 class="mb-0">""" +
            """<a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">""" +
            "Input" +
            s"""</a></h5></div><div id="collapseOne" class="collapse" role="tabpanel" aria-labelledby="headingOne"><div class="card-block"><pre>""" +
            monitor.dumpIn +
            s"""</pre></div></div></div><div class="card"><div class="card-header" role="tab" id="headingTwo"><h5 class="mb-0">""" +
            s"""<a class="collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo">""" +
            "Output" +
            s"""</a></h5></div><div id="collapseTwo" class="collapse" role="tabpanel" aria-labelledby="headingTwo"><div class="card-block"><pre>""" +
            monitor.dumOut +
            s"""</pre></div></div></div></div>"""
}
