organization in ThisBuild := "fr.davit"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.4"

val configs    = "com.github.kxbmap"        %% "configs"    % "0.4.4"
val enumeratum = "com.beachape"             %% "enumeratum" % "1.5.13"
val macwire    = "com.softwaremill.macwire" %% "macros"     % "2.3.0" % Provided
val scalaTest  = "org.scalatest"            %% "scalatest"  % "3.0.4" % Test

lazy val `tip-top` = (project in file("."))
  .aggregate(
    `tip-top-common`,
    `tip-top-user-api`,
    `tip-top-user-impl`,
    `tip-top-competition-api`,
    `tip-top-competition-impl`,
    `tip-top-provider-api`
  )

//----------------------------------------------------------------------------------------------------------------------
// Common
//----------------------------------------------------------------------------------------------------------------------
lazy val `tip-top-common` = (project in file("tip-top-common"))
  .settings(
    libraryDependencies ++= Seq(
      configs,
      enumeratum,
      lagomScaladslApi,
      scalaTest
    )
  )

//----------------------------------------------------------------------------------------------------------------------
// Competition
//----------------------------------------------------------------------------------------------------------------------
lazy val `tip-top-competition-api` = (project in file("tip-top-competition-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )
  .dependsOn(`tip-top-common`)

lazy val `tip-top-competition-impl` = (project in file("tip-top-competition-impl"))
  .enablePlugins(LagomScala, SbtReactiveAppPlugin)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      macwire,
      scalaTest
    )
  )
  .dependsOn(`tip-top-common`, `tip-top-competition-api`)

//----------------------------------------------------------------------------------------------------------------------
// User
//----------------------------------------------------------------------------------------------------------------------
lazy val `tip-top-user-api` = (project in file("tip-top-user-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )
  .dependsOn(`tip-top-common`)

lazy val `tip-top-user-impl` = (project in file("tip-top-user-impl"))
  .enablePlugins(LagomScala, SbtReactiveAppPlugin)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      macwire,
      scalaTest
    )
  )
  .dependsOn(`tip-top-common`, `tip-top-user-api`)

//----------------------------------------------------------------------------------------------------------------------
// Provider
//----------------------------------------------------------------------------------------------------------------------
lazy val `tip-top-provider-api` = (project in file("tip-top-provider-api"))
  .configs(IntegrationTest)
  .settings(
    Defaults.itSettings,
    libraryDependencies ++= Seq(
      lagomScaladslApi,
      lagomScaladslTestKit.withConfigurations(Some("it")),
      lagomScaladslClient.withConfigurations(Some("it")),
      scalaTest.withConfigurations(Some("it"))
    )
  )
  .dependsOn(`tip-top-common`)

lazy val `tip-top-provider-impl` = (project in file("tip-top-provider-impl"))
  .enablePlugins(LagomScala, SbtReactiveAppPlugin)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      macwire,
      scalaTest
    )
  )
  .dependsOn(`tip-top-common`, `tip-top-provider-api`, `tip-top-competition-api`)

//----------------------------------------------------------------------------------------------------------------------
// Unmanaged services
//----------------------------------------------------------------------------------------------------------------------
lagomUnmanagedServices in ThisBuild := Map("football-data" -> "http://api.football-data.org")
