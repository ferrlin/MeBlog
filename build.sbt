// The project name
name := "Meblog"

version := "0.1"

seq(webSettings: _*)

libraryDependencies ++= Seq(
    "com.mongodb.casbah" %% "casbah" % "2.1.2",
    "net.iharder" % "base64" % "2.3.8" % "compile->default",
    "org.fusesource.scalate" % "scalate-core" % "1.4.1"
)


libraryDependencies ++= Seq(
    "org.scalatest" % "scalatest" % "1.2",
    "org.scalatra" %% "scalatra" % "2.0.0.M3",
    "org.scalatra" %% "scalatra-auth" % "2.0.0.M3",
    "org.scalatra" %% "scalatra-scalate" % "2.0.0.M3"
)

libraryDependencies ++= Seq(
    "org.clapper" %% "grizzled-slf4j" % "0.4",
    "org.slf4j" % "slf4j-api" % "1.6.1" % "runtime->default",
    "org.slf4j" % "slf4j-nop" % "1.6.1" % "runtime->default"
)

libraryDependencies ++= Seq (
    "org.mortbay.jetty" % "jetty" % "6.1.22" % "jetty",
    "org.mortbay.jetty" % "servlet-api" % "2.5-20081211" % "provided->default"
)

unmanagedClasspath in Runtime <+= (scalaInstance) map { (scala) => Attributed.blank(scala.compilerJar) }
