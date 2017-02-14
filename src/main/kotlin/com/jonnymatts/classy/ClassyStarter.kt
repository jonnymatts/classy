package com.jonnymatts.classy

import de.jupf.staticlog.Log
import ro.pippo.core.Pippo
import java.util.jar.JarFile

fun main(args: Array<String>) {
    val jarFile: JarFile = JarFile("/usr/lib/jvm/java-8-oracle/jre/lib/rt.jar")
    val packages: Packages = Packages()
    val nonDirectories = jarFile.entries().toList().filter { !it.isDirectory }
    val noInnerClasses = nonDirectories.filter { !it.name.contains("$") }
    val metaInfRemoved = noInnerClasses.filter { !it.name.contains("META-INF") }
    metaInfRemoved.forEach { it -> packages.add(it.name.removeSuffix(".class")) }

    val port: Int = System.getenv("CLASSY_PORT")?.toInt() ?: 8080
    val pippo: Pippo = Pippo(PippoApplication(packages, Randomizer()))
    pippo.server.settings.host("0.0.0.0")
    pippo.start(port)
    Log.info("Started server on port: $port")
}