import sbt._
import sbt.Keys._
import PlayProject._
import scala.Some

object ApplicationBuild extends Build {

    val appName         = "io.svc.security.play.demo"
    val appVersion      = "1.1-SNAPSHOT"

    val appDependencies = Seq(
      "io.svc" %% "io-svc-security-play" % "0.2-SNAPSHOT" changing
    )

  val repositories =
    Seq("Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/",
        "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/")

  val ivyRepositories = Seq(Resolver.url("Local ivy", url("file:///var/lib/jenkins/.ivy2/local/"))(Resolver.ivyStylePatterns))

  val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(

    resolvers := repositories ++ ivyRepositories,

    credentials += Credentials(Path.userHome / ".m2" / "sonatype.credentials"),

    publishTo <<= version { (v: String) =>
      val nexus = "https://oss.sonatype.org/"
      if (v.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    },

    publishMavenStyle := true,

    //publishArtifact in Test := false,

    pomIncludeRepository := {
      x => false
    },

    testOptions in Test += Tests.Argument("junitxml")

  ).settings(
    addArtifact(Artifact(appName, "dist", "zip", "dist"), dist ).settings : _*
  )
}
