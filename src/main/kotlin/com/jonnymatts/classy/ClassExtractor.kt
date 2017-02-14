package com.jonnymatts.classy

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.jar.JarEntry
import java.util.jar.JarFile

class ClassExtractor {
    fun fromJar(jarFile: JarFile): List<ClassInfo> {
        return jarFile.entries().toList()
                .filter{ !it.isDirectory }
                .filter { !it.name.contains("$") }
                .map { createClassInfo(it) }
    }

    private fun createClassInfo(jarEntry: JarEntry): ClassInfo {
        val classNameWithPackage: String = jarEntry.name.removeSuffix(".class")
        val split: List<String> = classNameWithPackage.split("/")
        val className: String = split.last().removeSuffix(".class")
        val packageName: String = split.minus(className).joinToString(".")
        return ClassInfo(className, packageName)
    }
}

data class ClassInfo(val name: String, @JsonProperty("package") val fromPackage: String)