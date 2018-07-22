package fr.davit.tiptop.common.api.deser

import java.text.SimpleDateFormat
import java.util.Date

import enumeratum.{Enum, EnumEntry}
import com.lightbend.lagom.scaladsl.api.deser.{DefaultPathParamSerializers, PathParamSerializer}

object PathParamSerializers extends PathParamSerializers

trait PathParamSerializers extends DefaultPathParamSerializers {

  def datePathParamSerializer(format: String): PathParamSerializer[Date] = {
    val dateFormat = new SimpleDateFormat(format)
    required("Date")(dateFormat.parse)(dateFormat.format)
  }

  def enumPathParamSerializer[V <: EnumEntry, E <: Enum[V]](enum: E): PathParamSerializer[V] =
    required(enum.getClass.getSimpleName)(enum.withName)(_.entryName)
}
