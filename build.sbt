name := "FSMScalaAcademy"

version := "1.0"

scalaVersion := "2.12.4"

lazy val enumeratumVersion       = "1.5.12"
lazy val enumeratumJson4sVersion = "1.5.13"

libraryDependencies ++= Seq(
  "com.beachape" %% "enumeratum" % enumeratumVersion,
  "com.beachape" %% "enumeratum-json4s" % enumeratumJson4sVersion,
  "com.typesafe.akka" %% "akka-actor" % "2.5.6",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.6" % Test
)