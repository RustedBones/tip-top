package fr.davit.tiptop.provider.api

import com.lightbend.lagom.scaladsl.api.{Descriptor, Service}

trait ProviderService extends Service {

  override def descriptor: Descriptor = {
    import Service._
    named("provider")
  }

}
