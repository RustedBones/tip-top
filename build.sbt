organization in ThisBuild := "fr.davit"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.4"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4" % Test

lazy val `tip-top` = (project in file("."))
  .aggregate(
    `user-api`, `user-impl`,
    `match-api`, `match-impl`
  )


lazy val `common` = (project in file("common"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `user-api` = (project in file("user-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `user-impl` = (project in file("user-impl"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
    )
  ).dependsOn(`user-api`)

lazy val `match-api` = (project in file("match-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `match-impl` = (project in file("match-impl"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra
    )
  ).dependsOn(`match-api`)

//lazy val `tip-top-api` = (project in file("tip-top-api"))
//  .settings(
//    libraryDependencies ++= Seq(
//      lagomScaladslApi
//    )
//  )
//
//lazy val `tip-top-impl` = (project in file("tip-top-impl"))
//  .enablePlugins(LagomScala)
//  .settings(
//    libraryDependencies ++= Seq(
//      lagomScaladslPersistenceCassandra,
//      lagomScaladslKafkaBroker,
//      lagomScaladslTestKit,
//      macwire,
//      scalaTest
//    )
//  )
//  .settings(lagomForkedTestSettings: _*)
//  .dependsOn(`tip-top-api`)
//
//lazy val `tip-top-stream-api` = (project in file("tip-top-stream-api"))
//  .settings(
//    libraryDependencies ++= Seq(
//      lagomScaladslApi
//    )
//  )
//
//lazy val `tip-top-stream-impl` = (project in file("tip-top-stream-impl"))
//  .enablePlugins(LagomScala)
//  .settings(
//    libraryDependencies ++= Seq(
//      lagomScaladslTestKit,
//      macwire,
//      scalaTest
//    )
//  )
//  .dependsOn(`tip-top-stream-api`, `tip-top-api`)
