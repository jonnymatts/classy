package com.jonnymatts.classy

data class Package(val name: String, val children: MutableList<Package>, val classes: MutableList<String>)

class Packages(val packages: MutableList<Package>) {

    constructor() : this(mutableListOf())

    fun add(classNameWithPackage: String): Packages {
        val split: List<String> = classNameWithPackage.split("/")
        val className: String = split.last()
        val packageNames: List<String> = split.dropLast(1)
        val nearestPackage: Package? = findNearestMatchForPackageName(packageNames)
        if(nearestPackage == null) {
            packages.add(buildPackageStructure(packageNames.dropLast(1), Package(packageNames.last(), mutableListOf(), mutableListOf(className))))
            return this
        }
        val packagesToBeAdded: List<String> = packageNames.dropWhile { it != nearestPackage.name }.drop(1)
        if(packagesToBeAdded.isEmpty()) {
            nearestPackage.classes.add(className)
            return this
        }
        nearestPackage.children.add(buildPackageStructure(packagesToBeAdded.dropLast(1), Package(packageNames.last(), mutableListOf(), mutableListOf(className))))
        return this
    }

    private fun buildPackageStructure(packages: List<String>, childPackage: Package): Package {
        return packages.foldRight(childPackage, {name, child -> Package(name, mutableListOf(child), mutableListOf()) })
    }

    private fun findNearestMatchForPackageName(packageNames: List<String>): Package? {
        fun findNearestMatchForPackageInternal(packagesLeft: List<String>, foundPackage: Package): Package {
            val childPackages: List<Package> = foundPackage.children.filter { it.name == packagesLeft.first() }
            if(childPackages.isEmpty()) return foundPackage
            val childPackage: Package = childPackages.first()
            if(packagesLeft.size == 1) return childPackage
            return findNearestMatchForPackageInternal(packagesLeft.drop(1), childPackage)
        }

        val topLevelPackages: List<Package> = packages.filter { it.name == packageNames.first() }
        if(topLevelPackages.isEmpty()) return null

        return findNearestMatchForPackageInternal(packageNames.drop(1), topLevelPackages.first())
    }

    fun  getClassesForPackage(packageName: String): List<String> {
        val packageNames: List<String> = packageName.split(".")

        val foundPackage: Package? = packageNames.dropLast(1)
                .fold(packages, {
                    list, packageName -> list.filter { it.name == packageName }.flatMap { it.children }.toMutableList()
                })
                .filter { it.name == packageNames.last() }.firstOrNull()

        return foundPackage?.classes ?: emptyList()
    }
}