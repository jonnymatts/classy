package com.jonnymatts.classy

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.util.*
import java.util.Collections.enumeration
import java.util.jar.JarEntry
import java.util.jar.JarFile


class ClassExtractorTest {

    @Test
    fun fromJarReturnsListOfClassInfoFromJarFile() {
        val classExtractor: ClassExtractor = ClassExtractor()
        val jarFile: JarFile = mock(JarFile::class.java)

        val jarEntry1: JarEntry = JarEntry("com/example/blah.class")
        val jarEntry2: JarEntry = JarEntry("com/example/foo.class")
        val jarEntry3: JarEntry = JarEntry("com/package/bar.class")

        val enumeration: Enumeration<JarEntry> = enumeration(listOf(jarEntry1, jarEntry2, jarEntry3))

        `when`(jarFile.entries()).thenReturn(enumeration)

        val got: List<ClassInfo> = classExtractor.fromJar(jarFile)

        assertThat(got).hasSize(3)
        assertThat(got).contains(ClassInfo("blah", "com.example"))
        assertThat(got).contains(ClassInfo("foo", "com.example"))
        assertThat(got).contains(ClassInfo("bar", "com.package"))
    }

    @Test
    fun fromJarDoesNotReturnDirectoryJarEntries() {
        val classExtractor: ClassExtractor = ClassExtractor()
        val jarFile: JarFile = mock(JarFile::class.java)

        val jarEntry1: JarEntry = JarEntry("com/example/blah.class")
        val jarEntry2: JarEntry = JarEntry("com/example/")

        val enumeration: Enumeration<JarEntry> = enumeration(listOf(jarEntry1, jarEntry2))

        `when`(jarFile.entries()).thenReturn(enumeration)

        val got: List<ClassInfo> = classExtractor.fromJar(jarFile)

        assertThat(got).hasSize(1)
        assertThat(got).contains(ClassInfo("blah", "com.example"))
    }

    @Test
    fun fromJarDoesNotReturnJarEntriesWithDollarsInTheirNames() {
        val classExtractor: ClassExtractor = ClassExtractor()
        val jarFile: JarFile = mock(JarFile::class.java)

        val jarEntry1: JarEntry = JarEntry("com/example/blah.class")
        val jarEntry2: JarEntry = JarEntry("com/example/blah\$inner.class")

        val enumeration: Enumeration<JarEntry> = enumeration(listOf(jarEntry1, jarEntry2))

        `when`(jarFile.entries()).thenReturn(enumeration)

        val got: List<ClassInfo> = classExtractor.fromJar(jarFile)

        assertThat(got).hasSize(1)
        assertThat(got).contains(ClassInfo("blah", "com.example"))
    }
}