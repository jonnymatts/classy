package com.jonnymatts.classy

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PackagesTest {

    val defaultPackage: Package =
            Package(
                    "com",
                    mutableListOf(
                            Package("jonnymatts",
                                    mutableListOf(
                                            Package("classy",
                                                    mutableListOf(),
                                                    mutableListOf("Blah")
                                            )
                                    ),
                                    mutableListOf()
                            )
                    ),
                    mutableListOf()
            )

    @Test
    fun addReturnsPackagesWithNewPackageAdded() {
        val packages: Packages = Packages(mutableListOf())

        val got: Packages = packages.add("com/jonnymatts/classy/Blah")

        val packageList: MutableList<Package> = got.packages

        assertThat(packageList).hasSize(1)
        assertThat(packageList).containsExactly(
                defaultPackage)
    }

    @Test
    fun addReturnsPackagesWithNewClassAddedToExistingPackage() {

        val packages: Packages = Packages(mutableListOf(defaultPackage))

        val got: Packages = packages.add("com/jonnymatts/classy/Foo")

        val packageList: MutableList<Package> = got.packages

        assertThat(packageList).hasSize(1)
        assertThat(packageList).containsExactly(
                Package("com",
                        mutableListOf(
                                Package("jonnymatts",
                                        mutableListOf(
                                                Package("classy",
                                                        mutableListOf(),
                                                        mutableListOf("Blah", "Foo")
                                                )
                                        ),
                                        mutableListOf()
                                )
                        ),
                        mutableListOf()
                )
        )
    }
    @Test
    fun addReturnsPackagesWithNewClassAddedToParentPackageOfExistingPackage() {
        val packages: Packages = Packages(mutableListOf(defaultPackage))

        val got: Packages = packages.add("com/jonnymatts/Foo")

        val packageList: MutableList<Package> = got.packages

        assertThat(packageList).hasSize(1)
        assertThat(packageList).containsExactly(
                Package("com",
                        mutableListOf(
                                Package("jonnymatts",
                                        mutableListOf(
                                                Package("classy",
                                                        mutableListOf(),
                                                        mutableListOf("Blah")
                                                )
                                        ),
                                        mutableListOf("Foo")
                                )
                        ),
                        mutableListOf()
                )
        )
    }

    @Test
    fun getClassesForPackageReturnsListOfClassNamesIfPresent() {
        val packages: Packages = Packages(mutableListOf(defaultPackage))

        val got: List<String> = packages.getClassesForPackage("com.jonnymatts.classy")

        assertThat(got).hasSize(1)
        assertThat(got).containsExactly("Blah")
    }

    @Test
    fun getClassesForPackageReturnsEmptyListIfPackageIsNotFound() {
        val packages: Packages = Packages(mutableListOf(defaultPackage))

        val got: List<String> = packages.getClassesForPackage("com.blah")

        assertThat(got).isEmpty()
    }

    @Test
    fun getClassesForPackageReturnsEmptyListIfNoClassesArePresentForPackage() {
        val packages: Packages = Packages(mutableListOf(defaultPackage))

        val got: List<String> = packages.getClassesForPackage("com.jonnymatts")

        assertThat(got).isEmpty()
    }
}