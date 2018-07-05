package fr.davit.tiptop.`match`.api

import java.time.Instant
import java.util.UUID

import play.api.libs.json.{Format, Json}


final case class Team(name: String)

object Team {
  implicit val format: Format[Team] = Json.format[Team]
}

final case class Score(home: Int, away: Int)

object Score {
  implicit val format: Format[Score] = Json.format[Score]
}

final case class Match(id: UUID,
                       date: Instant,
                       homeTeam: Team,
                       awayTeam: Team,
                       status: Match.Status)

object ItemStatus extends Enumeration {
  val Scheduled, Playing, Finished, Cancelled = Value
  type Status = Value

  implicit val format: Format[Value] = enumFormat(this)
  implicit val pathParamSerializer: PathParamSerializer[Status] =
    PathParamSerializer.required("itemStatus")(withName)(_.toString)
}

object Match {

  sealed trait Status

  final case object Scheduled extends Status

  final case object Playing extends Status

  final case object Canceled extends Status

  final case onFinished extends Status


  implicit val matchFormat: Format[Match] = Json.format[Match]

}
