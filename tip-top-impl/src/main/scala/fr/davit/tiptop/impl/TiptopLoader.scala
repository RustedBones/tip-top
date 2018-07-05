package fr.davit.tiptop.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import fr.davit.tiptop.api.TiptopService
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.softwaremill.macwire._

class TiptopLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new TiptopApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new TiptopApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[TiptopService])
}

abstract class TiptopApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with LagomKafkaComponents
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[TiptopService](wire[TiptopServiceImpl])

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry = TiptopSerializerRegistry

  // Register the tip-top persistent entity
  persistentEntityRegistry.register(wire[TiptopEntity])
}
