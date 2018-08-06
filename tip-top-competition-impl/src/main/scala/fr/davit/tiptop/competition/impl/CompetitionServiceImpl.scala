package fr.davit.tiptop.competition.impl

import akka.persistence.query.Offset
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import fr.davit.tiptop.competition.api
import fr.davit.tiptop.competition.api.CompetitionService
import fr.davit.tiptop.competition.api.model.CompetitionId

import scala.concurrent.ExecutionContext

class CompetitionServiceImpl(registry: PersistentEntityRegistry)(implicit ec: ExecutionContext)
    extends CompetitionService {

  override def createCompetition = { competition =>
    val competitionId = competition.safeId
    val sport         = competition.sport
    val name          = competition.name
    val startDate     = competition.startDate
    val endDate       = competition.endDate

    val pCompetition = Competition(competitionId, sport, name, startDate, endDate)

    entityRef(competitionId)
      .ask(CreateCompetition(pCompetition))
      .map(convertCompetition)
  }

  override def createTeam(competitionId: CompetitionId) = { team =>
    val pTeam = Team(team.safeId, team.name)

    entityRef(competitionId)
      .ask(CreateTeam(pTeam))
      .map(convertTeam)
  }

  override def createMatch(competitionId: CompetitionId) = { `match` =>
    val pMatch = Match(`match`.safeId, `match`.date, `match`.homeTeamId, `match`.awayTeamId, `match`.status)

    entityRef(competitionId)
      .ask(CreateMatch(pMatch))
      .map(convertMatch)
  }

  registry.eventStream(CompetitionEvent.Tag, Offset.noOffset)

  private def convertCompetition(c: Competition): api.model.Competition = {
    api.model.Competition(Some(c.id), c.sport, c.name, c.startDate, c.endDate)
  }

  private def convertTeam(t: Team): api.model.Team = {
    api.model.Team(Some(t.id), t.name)
  }

  private def convertMatch(m: Match): api.model.Match = {
    api.model.Match(Some(m.id), m.date, m.homeTeamId, m.awayTeamId, m.status)
  }

  private def entityRef(competitionId: api.model.CompetitionId) = {
    registry.refFor[CompetitionEntity](competitionId.value.toString)
  }

}
