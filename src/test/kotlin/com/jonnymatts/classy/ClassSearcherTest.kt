package com.jonnymatts.classy

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ClassSearcherTest {

    @Test
    fun findClassesForPackageReturnsListOfClassInfo() {
        val classSearcher: ClassSearcher = ClassSearcher()

        val got: List<ClassInfo> = classSearcher.findClassesForPackage("javax.security")

        assertThat(got).hasSize(37)
        assertThat(got).contains(ClassInfo("Certificate", "javax.security.cert"))
    }
}