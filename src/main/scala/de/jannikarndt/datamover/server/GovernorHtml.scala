package de.jannikarndt.datamover.server

import de.jannikarndt.datamover.governance._

import scala.compat.Platform.EOL

object GovernorHtml {
    def getGovernedIds: String =
        """<div style="height: 200px;overflow: scroll;">""" +
            """<table class="table table-striped"><tr>""" +
            """<th>Identifier</th>""" +
            """<th>Jobname</th>""" +
            """<th>Started</th>""" +
            """</tr>""" +
            Governor.ids.map(getGovernedIdHtml).mkString(EOL) +
            """</table></div>"""

    def getGovernedIdHtml(governedID: GovernedID): String =
        """<tr>""" +
            s"""<td>${governedID.identifier}</td>""" +
            s"""<td>${governedID.jobname}</td>""" +
            s"""<td>${governedID.started}</td>""" +
            """</tr>"""
}
