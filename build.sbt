// ---------------------------------------------------------------------------
// Basic settings

name := "markwrap"

organization := "org.clapper"

version := "0.5.4"

licenses := Seq("BSD" -> url("http://software.clapper.org/markwrap/license.html"))

homepage := Some(url("http://software.clapper.org/markwrap/"))

description := (
  "A unified API for converting various lightweight markup languages to HTML"
)

scalaVersion := "2.9.1"

// ---------------------------------------------------------------------------
// Additional compiler options and plugins

scalacOptions ++= Seq("-deprecation", "-unchecked")

crossScalaVersions := Seq(
  "2.9.2", "2.9.1-1", "2.9.1", "2.9.0-1", "2.9.0", "2.8.2", "2.8.1", "2.8.0"
)

seq(lsSettings :_*)

(LsKeys.tags in LsKeys.lsync) := Seq(
  "markdown", "textile", "markup", "html", "library", "crap"
)

(description in LsKeys.lsync) <<= description(d => d)

// ---------------------------------------------------------------------------
// ScalaTest dependendency

libraryDependencies <<= (scalaVersion, libraryDependencies) { (sv, deps) =>
    // Select ScalaTest version based on Scala version
    val scalatestVersionMap = Map("2.8.0"   -> ("scalatest_2.8.0", "1.3.1.RC2"),
                                  "2.8.1"   -> ("scalatest_2.8.1", "1.7.1"),
                                  "2.8.2"   -> ("scalatest_2.8.2", "1.7.1"),
                                  "2.9.0"   -> ("scalatest_2.9.0", "1.7.1"),
                                  "2.9.0-1" -> ("scalatest_2.9.0-1", "1.7.1"),
                                  "2.9.1"   -> ("scalatest_2.9.1", "1.7.1"),
                                  "2.9.1-1" -> ("scalatest_2.9.1", "1.7.1"),
                                  "2.9.2"   -> ("scalatest_2.9.1", "1.7.1"))
    val (scalatestArtifact, scalatestVersion) = scalatestVersionMap.getOrElse(
        sv, error("Unsupported Scala version: " + scalaVersion)
    )
    deps :+ "org.scalatest" % scalatestArtifact % scalatestVersion % "test"
}

fork in Test := true

// ---------------------------------------------------------------------------
// Other dependendencies

libraryDependencies ++= Seq(
    "org.fusesource.wikitext" % "textile-core" % "1.3",
    "org.pegdown" % "pegdown" % "1.0.1"
)

// ---------------------------------------------------------------------------
// Publishing criteria

publishTo <<= version { v: String =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <scm>
    <url>git@github.com:bmc/markwrap.git/</url>
    <connection>scm:git:git@github.com:bmc/markwrap.git</connection>
  </scm>
  <developers>
    <developer>
      <id>bmc</id>
      <name>Brian Clapper</name>
      <url>http://www.clapper.org/bmc</url>
    </developer>
  </developers>
)
