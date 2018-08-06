package fr.davit.tiptop.provider.impl

import akka.Done
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import fr.davit.tiptop.competition.api.CompetitionService
import fr.davit.tiptop.provider.api.model.{ImportRequest, Provider}
import fr.davit.tiptop.provider.api.{FootballDataService, ProviderService}

class ProviderServiceImpl(registry: PersistentEntityRegistry,
                           competitionService: CompetitionService,
                           footballDataService: FootballDataService) extends ProviderService {

//  override def importCompetition: ServiceCall[ImportRequest, Done] = { request =>
//    request.provider match {
//      case Provider.FootballData =>
//        for {
//          c <- footballDataService.getCompetition(request.id.toLong).invoke()
//        } yield c
//    }
//  }
  override def importCompetition: ServiceCall[ImportRequest, Done] = ???
}
