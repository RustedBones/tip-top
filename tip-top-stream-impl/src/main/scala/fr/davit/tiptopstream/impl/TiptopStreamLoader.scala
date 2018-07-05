package fr.davit.tiptopstream.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import fr.davit.tiptopstream.api.TiptopStreamService
import fr.davit.tiptop.api.TiptopService
import com.softwaremill.macwire._

class TiptopStreamLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new TiptopStreamApplication(context) {
      override def serviceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new TiptopStreamApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[TiptopStreamService])
}

abstract class TiptopStreamApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[TiptopStreamService](wire[TiptopStreamServiceImpl])

  // Bind the TiptopService client
  lazy val tiptopService = serviceClient.implement[TiptopService]
}
