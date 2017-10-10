package de.jannikarndt.datamover.governance

import java.util.UUID

import de.jannikarndt.datamover.io.File
import play.api.libs.json.{Json, OWrites, Reads}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Governor {

    def getId(jobname: String): Future[GovernedID] = {
        Future {
            val identifier: UUID = UUID.randomUUID()
            val id = GovernedID(identifier, jobname)
            val json = Json.toJson(id)
            File.append("ids.json", json.toString())
            id
        }
    }
}

case class GovernedID(identifier: UUID, jobname: String)

object GovernedID {
    implicit val residentReads: Reads[GovernedID] = Json.reads[GovernedID]
    implicit val residentWrites: OWrites[GovernedID] = Json.writes[GovernedID]
}