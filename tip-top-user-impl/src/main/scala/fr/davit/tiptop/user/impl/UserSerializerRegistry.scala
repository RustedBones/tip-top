package fr.davit.tiptop.user.impl

import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}

object UserSerializerRegistry extends JsonSerializerRegistry {
  override def serializers = List(
    JsonSerializer[User],
    JsonSerializer[UserCreated],
    JsonSerializer[CreateUser],
    JsonSerializer[GetUser.type]
  )
}
