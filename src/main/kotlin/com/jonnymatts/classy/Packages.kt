package com.jonnymatts.classy

import com.fasterxml.jackson.annotation.JsonProperty

data class Package(val name: String, val children: MutableList<Package>, val classes: MutableList<ClassInfo>)
data class ClassInfo(val name: String, @JsonProperty("package") val fromPackage: String)

class Packages(val packages: MutableList<Package>) {

    constructor() : this(mutableListOf())

    fun add(classNameWithPackage: String): Packages {
        val split: List<String> = classNameWithPackage.split("/")
        val className: String = split.last()
        val packageNames: List<String> = split.dropLast(1)
        val classInfo: ClassInfo = ClassInfo(className, packageNames.joinToString(separator = "."))
        val nearestPackage: Package? = findNearestMatchForPackageName(packageNames)
        if (nearestPackage == null) {
            packages.add(buildPackageStructure(packageNames.dropLast(1), Package(packageNames.last(), mutableListOf(), mutableListOf(classInfo))))
            return this
        }
        val packagesToBeAdded: List<String> = packageNames.dropWhile { it != nearestPackage.name }.drop(1)
        if (packagesToBeAdded.isEmpty()) {
            nearestPackage.classes.add(classInfo)
            return this
        }
        nearestPackage.children.add(buildPackageStructure(packagesToBeAdded.dropLast(1), Package(packageNames.last(), mutableListOf(), mutableListOf(classInfo))))
        return this
    }

    private fun buildPackageStructure(packages: List<String>, childPackage: Package): Package {
        return packages.foldRight(childPackage, { name, child -> Package(name, mutableListOf(child), mutableListOf()) })
    }

    private fun findNearestMatchForPackageName(packageNames: List<String>): Package? {
        if(packageNames.isEmpty()) return null

        val topLevelPackage: Package? = packages.filter{it.name == packageNames.first()}.firstOrNull()

        return packageNames.drop(1).fold(topLevelPackage, {currentPackage, packageName ->
            val foundPackage: Package? = currentPackage?.children?.filter { it.name == packageName }?.firstOrNull()
            if(foundPackage == null) return currentPackage
            foundPackage
        })
    }

    fun getClassesForPackage(packageName: String): List<ClassInfo> {
        val packageNames: List<String> = packageName.split(".")
        val packagesToSearchIn: List<Package> = when (packageNames.size) {
            0, 1 -> packages
            else -> packageNames.dropLast(1)
                    .fold(packages, {
                        list, packageName ->
                        list.filter { it.name == packageName }.flatMap { it.children }.toMutableList()
                    })
        }

        val foundPackage: Package? =
                packagesToSearchIn
                        .filter { it.name == packageNames.last() }
                        .firstOrNull()

        return foundPackage?.classes?.plus(getAllNestedClasses(foundPackage)) ?: emptyList()
    }

    private fun getAllNestedClasses(topPackage: Package): List<ClassInfo> {
        val children: MutableList<Package> = topPackage.children
        if(children.isEmpty()) return emptyList()
        return children.flatMap { it.classes } + children.flatMap { getAllNestedClasses(it) }
    }
}