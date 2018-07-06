package fr.davit.tiptop.competition.api.model

import java.time.Instant
import java.util.UUID

import enumeratum.{Enum, EnumEntry}
import fr.davit.tiptop.common.json.JsonFormats._
import play.api.libs.json.{Format, Json}

import scala.collection.immutable

final case class MatchId(value: UUID) extends AnyVal
object MatchId {
  implicit val format: Format[MatchId] = valueFormat(MatchId.apply, _.value)
}


final case class Match(id: Option[MatchId],
                       date: Instant,
                       homeTeamId: TeamId,
                       awayTeamId: TeamId,
                       status: MatchStatus) {
  def safeId: MatchId = id.getOrElse(MatchId(UUID.randomUUID()))
}


object Match {
  implicit val format: Format[Match] = Json.format[Match]
}

sealed trait MatchStatus extends EnumEntry
object MatchStatus extends Enum[MatchStatus] {
  final case object Scheduled extends MatchStatus
  final case object Playing extends MatchStatus
  final case object Canceled extends MatchStatus
  final case object Finished extends MatchStatus

  override def values: immutable.IndexedSeq[MatchStatus] = findValues

  implicit val format: Format[MatchStatus] = enumFormat(MatchStatus)
}


final case class Score(home: Int, away: Int)

object Score {
  implicit val format: Format[Score] = Json.format[Score]
}
