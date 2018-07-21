package fr.davit.tiptop.common.json

import java.util.UUID

import enumeratum.{Enum, EnumEntry}
import play.api.libs.json._

import scala.concurrent.duration.Duration
import scala.util.Try

object JsonFormats {

  def valueReads[V <: AnyVal, T](apply: T => V)(implicit baseReads: Reads[T]): Reads[V] = {
    baseReads.map(apply)
  }

  def valueWrite[V <: AnyVal, T](extract: V => T)(implicit baseWrites: Writes[T]): Writes[V] = Writes { value =>
    baseWrites.writes(extract(value))
  }

  def valueFormat[V <: AnyVal, T](apply: T => V, extract: V => T)(
      implicit baseReads: Reads[T],
      baseWrites: Writes[T]): Format[V] = {
    Format(valueReads(apply), valueWrite(extract))
  }

  def enumReads[V <: EnumEntry, E <: Enum[V]](enum: E): Reads[V] = Reads {
    case JsString(s) =>
      enum.withNameOption(s) match {
        case Some(v) => JsSuccess(v)
        case None    => JsError(s"Enumeration expected of type: '$enum', but it does not contain '$s'")
      }
    case _ => JsError("String value expected")
  }

  def enumWrites[V <: EnumEntry, E <: Enum[V]]: Writes[V] = Writes(v => JsString(v.entryName))

  def enumFormat[V <: EnumEntry, E <: Enum[V]](enum: E): Format[V] = {
    Format(enumReads(enum), enumWrites)
  }

  def singletonReads[O](singleton: O): Reads[O] = {
    (__ \ "value")
      .read[String]
      .collect(
        JsonValidationError(
          s"Expected a JSON object with a single field with key 'value' and value '${singleton.getClass.getSimpleName}'")
      ) {
        case s if s == singleton.getClass.getSimpleName => singleton
      }
  }

  def singletonWrites[O]: Writes[O] = Writes { singleton =>
    Json.obj("value" -> singleton.getClass.getSimpleName)
  }

  def singletonFormat[O](singleton: O): Format[O] = {
    Format(singletonReads(singleton), singletonWrites)
  }

  implicit val uuidReads: Reads[UUID] = implicitly[Reads[String]]
    .collect(JsonValidationError("Invalid UUID"))(Function.unlift { str =>
      Try(UUID.fromString(str)).toOption
    })
  implicit val uuidWrites: Writes[UUID] = Writes { uuid =>
    JsString(uuid.toString)
  }

  implicit val durationReads: Reads[Duration] = implicitly[Reads[String]]
    .collect(JsonValidationError("Invalid duration"))(Function.unlift { str =>
      Try(Duration(str)).toOption
    })
  implicit val durationWrites: Writes[Duration] = Writes { duration =>
    JsString(duration.toString)
  }
}
