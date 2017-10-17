package de.jannikarndt.datamover.governance

import java.time.LocalDateTime
import java.util.UUID

import de.jannikarndt.datamover.io.File
import play.api.libs.json.{Json, OWrites, Reads}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

object Governor {

    val idStore = "output/governance_ids.json"

    var ids: Seq[GovernedID] = init()

    private def init() = {
        Try {
            val idsString: String = File.read(idStore)
            val ids = Json.parse(idsString).validate[Seq[GovernedID]]
            ids.get
        }.getOrElse(Seq[GovernedID]())
    }

    def getId(jobname: String): Future[GovernedID] = {
        Future {
            val id = new GovernedID(UUID.randomUUID(), jobname)
            storeId(id)
            id
        }
    }

    private def storeId(governedID: GovernedID): Unit = {
        ids = ids union Seq(governedID)
        File.overwrite(idStore, Json.toJson(ids).toString())
    }
}

case class GovernedID(identifier: UUID, jobname: String, started: LocalDateTime = LocalDateTime.now())

object GovernedID {
    implicit val residentReads: Reads[GovernedID] = Json.reads[GovernedID]
    implicit val residentWrites: OWrites[GovernedID] = Json.writes[GovernedID]
}