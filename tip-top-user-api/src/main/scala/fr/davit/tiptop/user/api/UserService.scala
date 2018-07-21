package fr.davit.tiptop.user.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}
import fr.davit.tiptop.user.api.model.{User, UserId}

trait UserService extends Service {
  def createUser: ServiceCall[User, User]

  def getUser(userId: UserId): ServiceCall[NotUsed, User]

  // Remove once we have a proper user service
  def getUsers: ServiceCall[NotUsed, Seq[User]]

  override def descriptor = {
    import Service._
    named("user").withCalls(
      pathCall("/api/user", createUser),
      pathCall("/api/user/:id", getUser _),
      pathCall("/api/user", getUsers)
    ).withAutoAcl(true)
  }
}
