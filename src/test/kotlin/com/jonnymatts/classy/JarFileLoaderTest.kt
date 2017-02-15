package com.jonnymatts.classy

import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.io.FileOutputStream
import java.util.jar.JarFile
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class JarFileLoaderTest {

    @JvmField
    @Rule
    val temporaryFolder: TemporaryFolder = TemporaryFolder()

    val jarFileLoader: JarFileLoader = JarFileLoader()

    @Test
    fun loadReturnsListOfJarFiles() {
        val file1: File = temporaryFolder.newFile()
        val file2: File = temporaryFolder.newFile()
        createZipFile(file1)
        createZipFile(file2)

        val jarFiles: List<JarFile> = jarFileLoader.load(listOf(file1.absolutePath, file2.absolutePath))

        assertThat(jarFiles).hasSize(2)
        assertThat(jarFiles[0].name).isEqualTo(file1.absolutePath)
        assertThat(jarFiles[1].name).isEqualTo(file2.absolutePath)
    }

    @Test
    fun loadReturnsListOfFoundJarFilesAndDoesNotThrowExceptionIfFindingJarFileThrowsException() {
        val file1: File = temporaryFolder.newFile()
        createZipFile(file1)

        val jarFiles: List<JarFile> = jarFileLoader.load(listOf(file1.absolutePath, "non/existent/file"))

        assertThat(jarFiles).hasSize(1)
        assertThat(jarFiles[0].name).isEqualTo(file1.absolutePath)
    }

    private fun createZipFile(file: File) {
        val zout: ZipOutputStream = ZipOutputStream(FileOutputStream(file))
        val zipEntry: ZipEntry = ZipEntry("entry.txt")
        zout.putNextEntry(zipEntry)
        zout.write("entry".toByteArray())
        zout.close()
    }
}