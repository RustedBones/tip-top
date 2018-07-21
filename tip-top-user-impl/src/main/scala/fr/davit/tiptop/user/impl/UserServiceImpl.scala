package fr.davit.tiptop.user.impl

import java.util.UUID

import akka.actor.ActorSystem
import akka.persistence.cassandra.query.scaladsl.CassandraReadJournal
import akka.persistence.query.PersistenceQuery
import akka.stream.Materializer
import akka.stream.scaladsl.Sink
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.NotFound
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import fr.davit.tiptop.user.api.UserService
import fr.davit.tiptop.user.api.model.UserId
import fr.davit.tiptop.user.api

import scala.concurrent.ExecutionContext

class UserServiceImpl(registry: PersistentEntityRegistry, system: ActorSystem)(
    implicit ec: ExecutionContext,
    mat: Materializer)
    extends UserService {

  private val currentIdsQuery =
    PersistenceQuery(system).readJournalFor[CassandraReadJournal](CassandraReadJournal.Identifier)

  override def createUser = ServiceCall { user =>
    val userId = user.safeId
    val pUser = User(userId, user.name)
    entityRef(userId)
        .ask(CreateUser(pUser))
        .map(convertUser)
  }

  override def getUser(userId: UserId) = ServiceCall { _ =>
    entityRef(userId)
      .ask(GetUser)
      .map {
        case Some(u) => convertUser(u)
        case None => throw NotFound(s"User with id $userId")
      }
  }

  override def getUsers = ServiceCall { _ =>
    // Note this should never make production....
    currentIdsQuery
      .currentPersistenceIds()
      .filter(_.startsWith("UserEntity|"))
      .mapAsync(4) { id =>
        val entityId = id.split("\\|", 2).last
        registry
          .refFor[UserEntity](entityId)
          .ask(GetUser)
          .map(_.map(convertUser))
      }
      .collect {
        case Some(user) => user
      }
      .runWith(Sink.seq)
  }

  private def convertUser(u: User): api.model.User = {
    api.model.User(Some(u.id), u.name)
  }

  private def entityRef(userId: UserId) = {
    registry.refFor[UserEntity](userId.value.toString)
  }
}
