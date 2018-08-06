package fr.davit.tiptop.competition.api.model

import java.time.Instant
import java.util.UUID

import com.lightbend.lagom.scaladsl.api.deser.PathParamSerializer
import fr.davit.tiptop.common.json.JsonFormats._
import play.api.libs.json.{Format, Json}

final case class CompetitionId(value: UUID) extends AnyVal

object CompetitionId {

  implicit val pathParamSerializer: PathParamSerializer[CompetitionId] = {
    PathParamSerializer.required("CompetitionId")(str => CompetitionId(UUID.fromString(str)))(_.value.toString)
  }

  implicit val format: Format[CompetitionId] = valueFormat(CompetitionId.apply, _.value)
}

final case class Competition(
    id: Option[CompetitionId],
    sport: Sport,
    name: String,
    startDate: Instant,
    endDate: Instant) {
  def safeId: CompetitionId = id.getOrElse(CompetitionId(UUID.randomUUID()))
}

object Competition {
  implicit val format: Format[Competition] = Json.format[Competition]
}
