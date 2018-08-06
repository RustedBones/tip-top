package fr.davit.tiptop.competition.impl

import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}

object CompetitionSerializerRegistry extends JsonSerializerRegistry {
  override def serializers = List(
    JsonSerializer[Competition],
    JsonSerializer[Team],
    JsonSerializer[Match]
  )
}
