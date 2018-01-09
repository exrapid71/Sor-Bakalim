name := """Sor-Bakalim"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.12.2"

libraryDependencies += filters
libraryDependencies += guice
libraryDependencies += jdbc


libraryDependencies += "com.h2database" % "h2" % "1.4.194"
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.41"

libraryDependencies += "org.mindrot" % "jbcrypt" % "0.3m"


libraryDependencies += "org.webjars.bower" % "jquery" % "3.2.1"
libraryDependencies += "org.webjars" % "bootstrap" % "3.3.4"


libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test
libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test
libraryDependencies += "org.mockito" % "mockito-core" % "2.1.0" % Test


testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
