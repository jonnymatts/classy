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
                                                    mutableListOf(ClassInfo("Blah", "com.jonnymatts.classy"))
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
                                                        mutableListOf(ClassInfo("Blah", "com.jonnymatts.classy"), ClassInfo("Foo", "com.jonnymatts.classy"))
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
                                                        mutableListOf(ClassInfo("Blah", "com.jonnymatts.classy"))
                                                )
                                        ),
                                        mutableListOf(ClassInfo("Foo", "com.jonnymatts"))
                                )
                        ),
                        mutableListOf()
                )
        )
    }

    @Test
    fun getClassesForPackageReturnsListOfClassNamesIfPresent() {
        val packages: Packages = Packages(mutableListOf(defaultPackage))

        val got: List<ClassInfo> = packages.getClassesForPackage("com.jonnymatts.classy")

        assertThat(got).hasSize(1)
        assertThat(got).containsExactly(ClassInfo("Blah", "com.jonnymatts.classy"))
    }

    @Test
    fun getClassesForPackageReturnsEmptyListIfPackageIsNotFound() {
        val packages: Packages = Packages(mutableListOf(defaultPackage))

        val got: List<ClassInfo> = packages.getClassesForPackage("com.blah")

        assertThat(got).isEmpty()
    }

    @Test
    fun getClassesForPackageReturnsEmptyListIfNoClassesArePresentForPackage() {
        val packages: Packages = Packages(mutableListOf(defaultPackage))

        val got: List<ClassInfo> = packages.getClassesForPackage("com.jonnymatts.classi")

        assertThat(got).isEmpty()
    }

    @Test
    fun getClassesForPackageReturnsReturnsListOfClassNamesIfPackageIsTopLevelPackage() {
        val packages: Packages = Packages(mutableListOf(defaultPackage))

        packages.add("com/jonnymatts/Bar")

        val got: List<ClassInfo> = packages.getClassesForPackage("com")

        assertThat(got).hasSize(2)
        assertThat(got).containsExactlyInAnyOrder(ClassInfo("Blah", "com.jonnymatts.classy"), ClassInfo("Bar", "com.jonnymatts"))
    }
}