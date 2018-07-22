package fr.davit.tiptop.provider.api

import java.net.URI

import com.lightbend.lagom.scaladsl.api.{Descriptor, ServiceLocator}
import com.lightbend.lagom.scaladsl.client.{
  CircuitBreakerComponents,
  CircuitBreakingServiceLocator,
  LagomClientApplication
}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import play.api.libs.ws.ahc.AhcWSComponents

import scala.concurrent.Future

class FootballDataServiceSpec extends FlatSpec with Matchers with ScalaFutures with BeforeAndAfterAll {

  implicit val defaultPatience =
    PatienceConfig(timeout = Span(2, Seconds), interval = Span(100, Millis))

  val clientApplication = new LagomClientApplication("test-client") with AhcWSComponents with CircuitBreakerComponents {
    override def serviceLocator: ServiceLocator =
      new CircuitBreakingServiceLocator(circuitBreakersPanel)(executionContext) {

        def getUri(name: String): Future[Option[URI]] = name match {
          case "football-data" => Future.successful(Some(URI.create("http://api.football-data.org")))
          case _               => Future.successful(None)
        }

        override def locate(name: String, serviceCall: Descriptor.Call[_, _]): Future[Option[URI]] =
          getUri(name)
      }
  }

  val fifaWorldCupId = 2000

  val client = clientApplication.serviceClient.implement[FootballDataService]

  override def afterAll(): Unit = {
    clientApplication.stop()
  }

  "FootballDataService" should "request the list of competitions" in {
    val competitionList = client.listCompetitions().invoke().futureValue
    competitionList.count should be > 0
    competitionList.competitions.size should be > 0
  }

  it should "request the list of teams in a competition" in {
    val teamList = client.listCompetitionTeams(fifaWorldCupId).invoke().futureValue
    teamList.count should be > 0
    teamList.teams.size should be > 0
  }

  it should "request the list of matches in a competition" in {
    val matchList = client.listCompetitionMatches(fifaWorldCupId).invoke().futureValue
    matchList.count should be > 0
    matchList.matches.size should be > 0
  }
}
