package fr.davit.tiptop.user.impl

import com.lightbend.lagom.scaladsl.persistence.PersistentEntity
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import fr.davit.tiptop.common.json.JsonFormats._
import fr.davit.tiptop.user.api.model.UserId
import play.api.libs.json._

class UserEntity extends PersistentEntity {
  override type Command = UserCommand[_]
  override type Event   = UserEvent
  override type State   = Option[User]
  override def initialState = None

  override def behavior: Behavior = {
    case None    => read orElse notCreated
    case Some(_) => read orElse defined
  }

  private val read = Actions()
    .onReadOnlyCommand[GetUser.type, Option[User]] {
      case (GetUser, ctx, state) =>
        ctx.reply(state)
    }

  private val notCreated = Actions()
    .onCommand[CreateUser, User] {
      case (CreateUser(user), ctx, state) =>
        ctx.thenPersist(UserCreated(user))(u => ctx.reply(u.user))
    }
    .onEvent {
      case (UserCreated(user), state) =>
        Some(user)
    }

  private val defined = Actions()
    .onReadOnlyCommand[CreateUser, User] {
      case (CreateUser(user), ctx, state) => ctx.invalidCommand("User already exists")
    }
}

//----------------------------------------------------------------------------------------------------------------------
// Model
//----------------------------------------------------------------------------------------------------------------------
case class User(id: UserId, name: String)

object User {
  implicit val format: Format[User] = Json.format
}

//----------------------------------------------------------------------------------------------------------------------
// Commands
//----------------------------------------------------------------------------------------------------------------------
sealed trait UserCommand[R] extends ReplyType[R]
case class CreateUser(user: User) extends UserCommand[User]

object CreateUser {
  implicit val format: Format[CreateUser] = Json.format
}

case object GetUser extends UserCommand[Option[User]] {
  implicit val format: Format[GetUser.type] = singletonFormat(GetUser)
}

//----------------------------------------------------------------------------------------------------------------------
// Events
//----------------------------------------------------------------------------------------------------------------------
sealed trait UserEvent
case class UserCreated(user: User) extends UserEvent

object UserCreated {
  implicit val format: Format[UserCreated] = Json.format
}
