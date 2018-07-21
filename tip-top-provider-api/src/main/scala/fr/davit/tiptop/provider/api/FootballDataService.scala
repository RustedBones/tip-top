package fr.davit.tiptop.provider.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

trait FootballDataService extends Service {

  def listCompetitions: ServiceCall[NotUsed, FootballData]

  override def descriptor: Descriptor = {
    import Service._
    named("competition").withCalls(
      pathCall("/api/competition", createCompetition _),
      pathCall("/api/competition/:id/team", createTeam _),
      pathCall("/api/competition/:id/match", createMatch _)
    ).withAutoAcl(true)
  }
}
