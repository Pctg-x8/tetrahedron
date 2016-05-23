name := "tetrafw"
version := "1.0.0-dev"
organization := "com.cterm2"
scalaVersion := "2.11.7"
val minecraftVersion = "1.7.10"
val forgeVersion = "10.13.4.1614"

// Compilation Options
scalaSource in Compile := baseDirectory.value / "src"
scalacOptions ++= Seq("-deprecation", "-encoding", "UTF8", "-feature")

// Dependencies
libraryDependencies ++= Seq(
	"org.apache.logging.log4j" % "log4j-api" % "2.0-beta9",
	"net.minecraft" % "launchwrapper" % "1.12",
	"com.mojang" % "authlib" % "1.5.16",
	"lzma" % "lzma" % "0.0.1",
	"io.netty" % "netty-all" % "4.0.10.Final",
	"net.sf.trove4j" % "trove4j" % "3.0.3",
	"com.ibm.icu" % "icu4j-core-mojang" % "51.2",
	"com.paulscode" % "soundsystem" % "20120107",
	"com.paulscode" % "librarylwjglopenal" % "20100824",
	"com.paulscode" % "codecjorbis" % "20101023",
	"org.lwjgl.lwjgl" % "lwjgl_util" % "2.9.1"
)
resolvers += "minecraft" at "https://libraries.minecraft.net"
val srcJarVersionSignature = Seq(minecraftVersion, forgeVersion, minecraftVersion) mkString "-"
val gradleCaches = file(System.getProperty("user.home")) / ".gradle" / "caches"
val forgeSources = Def.setting { gradleCaches / "minecraft" / "net" / "minecraftforge" / "forge" / srcJarVersionSignature }
unmanagedJars in Compile += forgeSources.value / ("forgeSrc-" + srcJarVersionSignature + ".jar")
unmanagedBase in Compile := (baseDirectory in Compile).value / "libs"
