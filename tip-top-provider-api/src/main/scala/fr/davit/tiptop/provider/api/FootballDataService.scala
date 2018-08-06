package fr.davit.tiptop.provider.api

import java.time.Instant
import java.util.Date

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.deser.PathParamSerializer
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import com.typesafe.config.ConfigFactory
import fr.davit.tiptop.common.api.deser.PathParamSerializers
import fr.davit.tiptop.common.api.transport.AuthTokenHeaderFilter
import fr.davit.tiptop.common.api.transport.AuthTokenHeaderFilter.{AuthTokenHeader, AuthTokenHeaderSettings}
import fr.davit.tiptop.provider.api.model.FootballData

trait FootballDataService extends Service {

  private val config                           = ConfigFactory.load()
  private val authTokenHeader: AuthTokenHeader = new AuthTokenHeaderSettings(config, "football-data.token")

  implicit val datePathParamSerializer: PathParamSerializer[Date] =
    PathParamSerializers.datePathParamSerializer("YYYY-MM-dd")
  implicit val statusPathParamSerializer: PathParamSerializer[FootballData.MatchStatus] =
    PathParamSerializers.enumPathParamSerializer(FootballData.MatchStatus)

  def listCompetitions(areas: Option[Seq[Long]] = None): ServiceCall[NotUsed, FootballData.CompetitionList]

  def getCompetition(competitionId: Long): ServiceCall[NotUsed, FootballData.Competition]

  def listCompetitionTeams(competitionId: Long): ServiceCall[NotUsed, FootballData.CompetitionTeamList]

  def listCompetitionMatches(
      competitionId: Long,
      dateFrom: Option[Date] = None,
      dateTo: Option[Date] = None,
      status: Option[FootballData.MatchStatus] = None): ServiceCall[NotUsed, FootballData.CompetitionMatchList]

  override def descriptor: Descriptor = {
    import Service._
    named("football-data")
      .withCalls(
        pathCall("/v2/competitions?areas", listCompetitions _),
        pathCall("/v2/competitions/:id", getCompetition _),
        pathCall("/v2/competitions/:id/teams", listCompetitionTeams _),
        pathCall("/v2/competitions/:id/matches?dateFrom&dateTo&status", listCompetitionMatches _)
      )
      .withHeaderFilter(new AuthTokenHeaderFilter(authTokenHeader))
      .withAutoAcl(true)
  }
}
