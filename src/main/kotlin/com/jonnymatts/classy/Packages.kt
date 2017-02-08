package com.jonnymatts.classy

class Packages(val packages: MutableList<Package>) {

    fun add(classNameWithPackage: String): Packages {
        val split: List<String> = classNameWithPackage.split("/")
        val className: String = split.last()
        val packageNames: List<String> = split.dropLast(1)
        packages.add(buildPackageStructure(packageNames.dropLast(1), Package(packageNames.last(), emptyList(), listOf(className))))
        return this
    }

    private fun buildPackageStructure(packages: List<String>, childPackage: Package): Package {
        return packages.foldRight(childPackage, {name, child -> Package(name, listOf(child), emptyList())})
    }
}