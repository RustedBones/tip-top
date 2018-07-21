package fr.davit.tiptop.competition.api.model

import java.util.UUID

import fr.davit.tiptop.common.json.JsonFormats._
import play.api.libs.json.{Format, Json}

final case class TeamId(value: UUID) extends AnyVal

object TeamId {
  implicit val format: Format[TeamId] = valueFormat(TeamId.apply, _.value)
}

final case class Team(id: Option[TeamId], name: String) {
  def safeId: TeamId = id.getOrElse(TeamId(UUID.randomUUID()))
}

object Team {
  implicit val format: Format[Team] = Json.format[Team]
}
