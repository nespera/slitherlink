name := """slitherlink"""

version := "1.0-SNAPSHOT"

lazy val root = project in file(".")

scalaVersion := "2.12.4"

libraryDependencies ++= Seq("org.specs2" %% "specs2-core" % "4.0.0" % "test")

scalacOptions in Test ++= Seq("-Yrangepos")