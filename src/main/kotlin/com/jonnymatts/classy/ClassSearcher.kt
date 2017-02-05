package com.jonnymatts.classy

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.common.reflect.ClassPath

class ClassSearcher {

    fun findClassesForPackage(packageName: String): List<ClassInfo> {
        val classLoader = Thread.currentThread().contextClassLoader
        val classPath: ClassPath = ClassPath.from(classLoader)
        val classNames = classPath.getTopLevelClassesRecursive(packageName)
        return classNames.map{it -> ClassInfo(it.simpleName, it.packageName)}
    }
}

data class ClassInfo(val name: String, @JsonProperty("package") val fromPackage: String)