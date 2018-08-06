package fr.davit.tiptop.competition.api

import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import fr.davit.tiptop.competition.api.model.{Competition, CompetitionId, Match, Team}

trait CompetitionService extends Service {

  def createCompetition: ServiceCall[Competition, Competition]

  def createTeam(competitionId: CompetitionId): ServiceCall[Team, Team]

  def createMatch(competitionId: CompetitionId): ServiceCall[Match, Match]

  override def descriptor: Descriptor = {
    import Service._
    named("competition").withCalls(
      pathCall("/api/competition", createCompetition),
      pathCall("/api/competition/:id/team", createTeam _),
      pathCall("/api/competition/:id/match", createMatch _)
    ).withAutoAcl(true)
  }
}
