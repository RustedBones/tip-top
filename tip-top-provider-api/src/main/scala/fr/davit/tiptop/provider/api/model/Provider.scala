package fr.davit.tiptop.provider.api.model

import enumeratum.{Enum, EnumEntry}
import fr.davit.tiptop.common.json.JsonFormats._
import play.api.libs.json.{Format, Json}

import scala.collection.immutable

sealed trait Provider extends EnumEntry

object Provider extends Enum[Provider] {

  object FootballData extends Provider

  override val values: immutable.IndexedSeq[Provider] = findValues

  implicit val format: Format[Provider] = enumFormat(Provider)
}

final case class ImportRequest(
    provider: Provider,
    id: String
)

object ImportRequest {
  implicit val format: Format[ImportRequest] = Json.format[ImportRequest]
}
