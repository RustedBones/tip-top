package fr.davit.tiptop.provider.impl

import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader}
import com.lightbend.rp.servicediscovery.lagom.scaladsl.LagomServiceLocatorComponents
import com.softwaremill.macwire._
import fr.davit.tiptop.competition.api.CompetitionService
import fr.davit.tiptop.provider.api.{FootballDataService, ProviderService}
import play.api.libs.ws.ahc.AhcWSComponents

abstract class ProviderApplication(context: LagomApplicationContext)
    extends LagomApplication(context)
    with AhcWSComponents
    with CassandraPersistenceComponents {

  lazy val competitionService   = serviceClient.implement[CompetitionService]
  lazy val footballDataService  = serviceClient.implement[FootballDataService]

  override lazy val lagomServer = serverFor[ProviderService](wire[ProviderServiceImpl])
  override lazy val jsonSerializerRegistry = ProviderSerializerRegistry

  persistentEntityRegistry.register(wire[ProviderEntity])
}

class ProviderApplicationLoader extends LagomApplicationLoader {
  override def load(context: LagomApplicationContext) =
    new ProviderApplication(context) with LagomServiceLocatorComponents

  override def loadDevMode(context: LagomApplicationContext) =
    new ProviderApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[ProviderService])
}
