package fr.davit.tiptop.common

import com.typesafe.config.{Config, ConfigFactory}
import configs.Result
import configs.Result.Failure
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._

object SettingsSpec {
  final case class CaseClass(str: String)
}

class SettingsSpec extends FlatSpec with Matchers {

  import SettingsSpec._

  val config: Config = ConfigFactory.parseString("""
                                                   |test {
                                                   |
                                                   |  case-class {
                                                   |    str = value
                                                   |  }
                                                   |
                                                   |  number = 12
                                                   |
                                                   |  duration = 42 seconds
                                                   |}
                                                 """.stripMargin)

  final class TestSettings(config: Config) extends Settings(config, "test") {

    val caseClass: CaseClass = get[CaseClass]("case-class")

    val number: Int = get[Int]("number")

    val duration: FiniteDuration = get[FiniteDuration]("duration")

    val invalid: Result[String] = read[String]("invalid")
  }

  "Settings" should "extract the settings from a hocon config file" in {
    val settings = new TestSettings(config)

    settings.caseClass shouldBe CaseClass("value")
    settings.number shouldBe 12
    settings.duration shouldBe 42.seconds
    settings.invalid shouldBe a[Failure]
  }
}
