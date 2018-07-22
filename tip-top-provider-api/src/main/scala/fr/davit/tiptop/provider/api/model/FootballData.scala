package fr.davit.tiptop.provider.api.model

import java.time.Instant

import com.lightbend.lagom.scaladsl.api.deser.PathParamSerializer
import enumeratum.EnumEntry.UpperSnakecase
import enumeratum.{Enum, EnumEntry}
import fr.davit.tiptop.common.json.JsonFormats
import play.api.libs.json.{Format, Json}

import scala.collection.immutable

object FootballData {

  final case class CompetitionList(
      count: Int,
      competitions: immutable.Seq[Competition]
  )

  object CompetitionList {
    implicit val format: Format[CompetitionList] = Json.format[CompetitionList]
  }

  final case class Competition(
      id: Long,
      name: String,
      lastUpdated: Instant
  )

  object Competition {
    implicit val format: Format[Competition] = Json.format[Competition]
  }

  final case class CompetitionTeamList(
      count: Int,
      teams: immutable.Seq[Team]
  )

  object CompetitionTeamList {
    implicit val format: Format[CompetitionTeamList] = Json.format[CompetitionTeamList]
  }

  final case class Team(
      id: Long,
      name: String,
      shortName: Option[String],
      tla: Option[String]
  )

  object Team {
    implicit val format: Format[Team] = Json.format[Team]
  }

  final case class CompetitionMatchList(
      count: Int,
      matches: immutable.Seq[Match]
  )

  object CompetitionMatchList {
    implicit val format: Format[CompetitionMatchList] = Json.format[CompetitionMatchList]
  }


  sealed trait MatchStatus extends EnumEntry with UpperSnakecase
  object MatchStatus extends Enum[MatchStatus] {
    final case object Scheduled extends MatchStatus
    final case object Live extends MatchStatus
    final case object InPlay extends MatchStatus
    final case object Paused extends MatchStatus
    final case object Finished extends MatchStatus
    final case object Postponed extends MatchStatus
    final case object Suspended extends MatchStatus
    final case object Canceled extends MatchStatus

    override val values: immutable.IndexedSeq[MatchStatus] = findValues

    implicit val format: Format[MatchStatus] = JsonFormats.enumFormat(MatchStatus)

    implicit val pathParamSerializer: PathParamSerializer[MatchStatus] = PathParamSerializer
      .required("MatchStatus")(MatchStatus.withName)(_.entryName)
  }

  final case class Match(
      id: Long,
      status: MatchStatus,
      homeTeam: Option[Team],
      awayTeam: Option[Team]
  )

  object Match {
    implicit val format: Format[Match] = Json.format[Match]
  }

}
