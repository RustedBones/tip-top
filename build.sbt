

organization in ThisBuild := "fr.davit"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.4"

val enumeratum = "com.beachape"             %% "enumeratum" % "1.5.13"
val macwire    = "com.softwaremill.macwire" %% "macros"     % "2.3.0" % "provided"
val scalaTest  = "org.scalatest"            %% "scalatest"  % "3.0.4" % Test

lazy val `tip-top` = (project in file("."))
  .aggregate(
    `common`,
    `user-api`,
    `user-impl`,
    `competition-api`,
    `competition-impl`
  )

lazy val `common` = (project in file("common"))
  .settings(
    libraryDependencies ++= Seq(
      enumeratum,
      lagomScaladslApi
    )
  )

lazy val `competition-api` = (project in file("competition-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )
  .dependsOn(`common`)

lazy val `competition-impl` = (project in file("competition-impl"))
  .enablePlugins(LagomScala, SbtReactiveAppPlugin)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      macwire,
      scalaTest
    )
  )
  .dependsOn(`common`, `competition-api`)

lazy val `user-api` = (project in file("user-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  ).dependsOn(`common`)

lazy val `user-impl` = (project in file("user-impl"))
  .enablePlugins(LagomScala, SbtReactiveAppPlugin)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      macwire,
      scalaTest
    )
  )
  .dependsOn(`common`, `user-api`)
