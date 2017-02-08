package com.jonnymatts.classy

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PackagesTest {

    @Test
    fun addReturnsPackagesWithNewPackageAdded() {
        val packages: Packages = Packages(mutableListOf())

        val got: Packages = packages.add("com/jonnymatts/classy/Blah")

        val packageList: MutableList<Package> = got.packages

        assertThat(packageList).hasSize(1)
        assertThat(packageList).containsExactly(
                Package("com",
                        listOf(
                                Package("jonnymatts",
                                        listOf(
                                                Package("classy",
                                                        emptyList(),
                                                        listOf("Blah")
                                                )
                                        ),
                                        emptyList()
                                )
                        ),
                        emptyList()
                )
        )
    }

    @Test
    fun addReturnsPackagesWithNewClassAddedToExistingPackage() {
        val currentPackages: MutableList<Package> = mutableListOf(
                Package("com",
                        listOf(
                                Package("jonnymatts",
                                        listOf(
                                                Package("classy",
                                                        emptyList(),
                                                        listOf("Blah")
                                                )
                                        ),
                                        emptyList()
                                )
                        ),
                        emptyList()
                )
        )

        val packages: Packages = Packages(currentPackages)

        val got: Packages = packages.add("com/jonnymatts/classy/Foo")

        val packageList: MutableList<Package> = got.packages

        assertThat(packageList).hasSize(1)
        assertThat(packageList).containsExactly(
                Package("com",
                        listOf(
                                Package("jonnymatts",
                                        listOf(
                                                Package("classy",
                                                        emptyList(),
                                                        listOf("Blah", "Foo")
                                                )
                                        ),
                                        emptyList()
                                )
                        ),
                        emptyList()
                )
        )
    }
}