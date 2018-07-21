package fr.davit.tiptop.user.api.model

import java.util.UUID

import com.lightbend.lagom.scaladsl.api.deser.PathParamSerializer
import play.api.libs.json.{Format, Json}
import fr.davit.tiptop.common.json.JsonFormats._

final case class UserId(value: UUID) extends AnyVal

object UserId {

  implicit val pathParamSerializer: PathParamSerializer[UserId] = {
    PathParamSerializer.required("UserId")(str => UserId(UUID.fromString(str)))(_.value.toString)
  }

  implicit val format: Format[UserId] = {
    valueFormat(UserId.apply, _.value)
  }
}

case class User(id: Option[UserId], name: String) {
  def safeId: UserId = id.getOrElse(UserId(UUID.randomUUID()))
}

object User {
  implicit val format: Format[User] = Json.format
}