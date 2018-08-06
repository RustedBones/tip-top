package fr.davit.tiptop.competition.api.model

import fr.davit.tiptop.common.json.JsonFormats._
import enumeratum.{Enum, EnumEntry}
import play.api.libs.json.Format

import scala.collection.immutable

sealed trait Sport extends EnumEntry

object Sport extends Enum[Sport]  {
  object FootBall extends Sport

  override val values: immutable.IndexedSeq[Sport] = findValues

  implicit val format: Format[Sport] = enumFormat(Sport)
}
