package fr.davit.tiptop.competition.impl

import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader}
import com.lightbend.rp.servicediscovery.lagom.scaladsl.LagomServiceLocatorComponents
import com.softwaremill.macwire._
import fr.davit.tiptop.competition.api.CompetitionService
import play.api.libs.ws.ahc.AhcWSComponents

abstract class CompetitionApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents
    with CassandraPersistenceComponents {

  override lazy val lagomServer = serverFor[CompetitionService](wire[CompetitionServiceImpl])
  override lazy val jsonSerializerRegistry = CompetitionSerializerRegistry

  persistentEntityRegistry.register(wire[CompetitionEntity])
}

class CompetitionApplicationLoader extends LagomApplicationLoader {
  override def load(context: LagomApplicationContext) =
    new CompetitionApplication(context) with LagomServiceLocatorComponents

  override def loadDevMode(context: LagomApplicationContext) =
    new CompetitionApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[CompetitionService])
}
