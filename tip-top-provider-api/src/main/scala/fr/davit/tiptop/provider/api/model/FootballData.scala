package fr.davit.tiptop.provider.api.model

import java.time.Instant

import scala.collection.immutable

object FootballData {

  final case class CompetitionList(
      count: Int,
      competitions: immutable.Seq[Competition]
  )

  final case class Competition(
      id: Long,
      name: String,
      lastUpdated: Instant
  )

}
