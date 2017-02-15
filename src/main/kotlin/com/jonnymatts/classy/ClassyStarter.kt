package com.jonnymatts.classy

import de.jupf.staticlog.Log
import ro.pippo.core.Pippo
import java.util.jar.JarEntry
import java.util.jar.JarFile

fun main(args: Array<String>) {
    val packages: Packages = Packages()
    val jarFileLoader: JarFileLoader = JarFileLoader()
    val jarFiles: List<JarFile> = jarFileLoader.load(args.asList())
    val nonDirectories = jarFiles.flatMap { it.entries().toList() }.filter { !it.isDirectory }
    val noInnerClasses = nonDirectories.filter { !it.name.contains("$") }
    val metaInfRemoved = noInnerClasses.filter { !it.name.contains("META-INF") }
    val onlyClasses: List<JarEntry> = metaInfRemoved.filter { it.name.contains("/") }
    onlyClasses.forEach { it -> packages.add(it.name.removeSuffix(".class")) }

    val port: Int = System.getenv("CLASSY_PORT")?.toInt() ?: 8080
    val pippo: Pippo = Pippo(PippoApplication(packages, Randomizer()))
    pippo.server.settings.host("0.0.0.0")
    pippo.start(port)
    Log.info("Started server on port: $port")
}