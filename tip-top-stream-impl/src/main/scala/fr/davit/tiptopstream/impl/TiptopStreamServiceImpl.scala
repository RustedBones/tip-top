package fr.davit.tiptopstream.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import fr.davit.tiptopstream.api.TiptopStreamService
import fr.davit.tiptop.api.TiptopService

import scala.concurrent.Future

/**
  * Implementation of the TiptopStreamService.
  */
class TiptopStreamServiceImpl(tiptopService: TiptopService) extends TiptopStreamService {
  def stream = ServiceCall { hellos =>
    Future.successful(hellos.mapAsync(8)(tiptopService.hello(_).invoke()))
  }
}
