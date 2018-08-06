package fr.davit.tiptop.provider.impl

import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry

object ProviderSerializerRegistry extends JsonSerializerRegistry {
  override def serializers = List()
}
