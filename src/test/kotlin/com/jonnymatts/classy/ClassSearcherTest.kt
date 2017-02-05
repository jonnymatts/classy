package com.jonnymatts.classy

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ClassSearcherTest {

    @Test
    fun findClassesForPackageReturnsListOfClassInfo() {
        val classSearcher: ClassSearcher = ClassSearcher()

        val got: List<ClassInfo> = classSearcher.findClassesForPackage("com.jonnymatts.classy")

        assertThat(got).hasSize(9)
        assertThat(got).contains(ClassInfo("ClassSearcher", "com.jonnymatts.classy"))
    }
}