package com.jonnymatts.classy

import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner

class ClassSearcher {

    fun findClassesForPackage(packageName: String): List<ClassInfo> {
        val reflections: Reflections = Reflections(packageName, SubTypesScanner(false))
        val classNames: MutableSet<String> = reflections.allTypes
        return classNames
                .filter{!it.contains("$")}
                .map{createClassInfo(it)}
    }

    private fun createClassInfo(classNameWithPackage: String): ClassInfo {
        val split: List<String> = classNameWithPackage.split(".")
        val className: String = split.last()
        val packageName: String = split.minus(className).joinToString(".")
        return ClassInfo(className, packageName)
    }
}

data class ClassInfo(val name: String, val fromPackage: String)