package fr.davit.tiptop.competition.impl

import java.time.Instant

import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, AggregateEventTagger, PersistentEntity}
import fr.davit.tiptop.competition.api.model._
import play.api.libs.json.{Format, Json}

class CompetitionEntity extends PersistentEntity {
  override type Command = CompetitionCommand[_]
  override type Event   = CompetitionEvent
  override type State   = CompetitionState

  override def initialState: State = CompetitionState()

  override def behavior: Behavior = {
    case CompetitionState(None, _, _) =>
      notCreated
    case CompetitionState(Some(c), _, _) =>
      if (c.endDate.isBefore(Instant.now())) {
        open
      } else {
        closed
      }
  }

  private val notCreated = Actions()
    .onCommand[CreateCompetition, Competition] {
      case (CreateCompetition(c), ctx, state) =>
        ctx.thenPersist(CompetitionCreated(c))(e => ctx.reply(e.competition))
    }
    .onEvent {
      case (CompetitionCreated(c), state) =>
        state.withCompetition(c)
    }

  private val open = Actions()
    .onCommand[CreateTeam, Team] {
      case (CreateTeam(t), ctx, state) =>
        ctx.thenPersist(TeamCreated(t))(e => ctx.reply(e.team))
    }
    .onCommand[CreateMatch, Match] {
      case (CreateMatch(m), ctx, state) =>
        ctx.thenPersist(MatchCreated(m))(e => ctx.reply(e.`match`))
    }
    .onEvent {
      case (TeamCreated(t), state) =>
        state.withTeam(t)
      case (MatchCreated(m), state) =>
        state.withMatch(m)
    }

  private val closed = Actions()
}


//----------------------------------------------------------------------------------------------------------------------
// Model
//----------------------------------------------------------------------------------------------------------------------
final case class Competition(
    id: CompetitionId,
    sport: Sport,
    name: String,
    startDate: Instant,
    endDate: Instant
)

object Competition {
  implicit val format: Format[Competition] = Json.format[Competition]
}

final case class Team(id: TeamId, name: String)

object Team {
  implicit val format: Format[Team] = Json.format[Team]
}

final case class Match(id: MatchId, date: Instant, homeTeamId: TeamId, awayTeamId: TeamId, status: MatchStatus)

object Match {
  implicit val format: Format[Match] = Json.format[Match]
}

//----------------------------------------------------------------------------------------------------------------------
// Commands
//----------------------------------------------------------------------------------------------------------------------
sealed trait CompetitionCommand[R] extends ReplyType[R]
final case class CreateCompetition(competition: Competition) extends CompetitionCommand[Competition]

final case class CreateTeam(team: Team) extends CompetitionCommand[Team]

final case class CreateMatch(`match`: Match) extends CompetitionCommand[Match]

//----------------------------------------------------------------------------------------------------------------------
// Events
//----------------------------------------------------------------------------------------------------------------------
sealed trait CompetitionEvent extends AggregateEvent[CompetitionEvent] {
  override def aggregateTag: AggregateEventTagger[CompetitionEvent] = CompetitionEvent.Tag
}
object CompetitionEvent {
  val Tag = AggregateEventTag[CompetitionEvent]
}

final case class CompetitionCreated(competition: Competition) extends CompetitionEvent
final case class TeamCreated(team: Team) extends CompetitionEvent
final case class MatchCreated(`match`: Match) extends CompetitionEvent

//----------------------------------------------------------------------------------------------------------------------
// State
//----------------------------------------------------------------------------------------------------------------------
final case class CompetitionState(
    competition: Option[Competition] = None,
    teams: Map[TeamId, Team] = Map.empty,
    matches: Map[MatchId, Match] = Map.empty
) {
  def withCompetition(competition: Competition): CompetitionState = {
    copy(competition = Some(competition))
  }

  def withTeam(team: Team): CompetitionState = {
    copy(teams = teams + (team.id -> team))
  }

  def withMatch(`match`: Match): CompetitionState = {
    copy(matches = matches + (`match`.id -> `match`))
  }
}
