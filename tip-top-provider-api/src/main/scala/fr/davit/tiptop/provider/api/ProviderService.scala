package fr.davit.tiptop.provider.api

import akka.Done
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import fr.davit.tiptop.provider.api.model.ImportRequest

trait ProviderService extends Service {

  def importCompetition: ServiceCall[ImportRequest, Done]

  override def descriptor: Descriptor = {
    import Service._
    named("provider").withCalls(
      pathCall("/api/import", importCompetition)
    ).withAutoAcl(true)
  }

}
