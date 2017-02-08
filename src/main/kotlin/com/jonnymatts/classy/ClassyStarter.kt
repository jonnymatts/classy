package com.jonnymatts.classy

import de.jupf.staticlog.Log
import ro.pippo.core.Pippo

fun main(args: Array<String>) {
    val port: Int = System.getenv("CLASSY_PORT")?.toInt() ?: 8080
    val pippo: Pippo = Pippo(PippoApplication(listOf(ClassInfo("Blah", "com.jonnymatts.classy")), Randomizer()))
    pippo.server.settings.host("0.0.0.0")
    pippo.start(port)
    Log.info("Started server on port: $port")
}