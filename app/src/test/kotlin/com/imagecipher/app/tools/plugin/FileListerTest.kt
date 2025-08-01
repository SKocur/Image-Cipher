package com.imagecipher.app.tools.plugin

import com.imagecipher.app.plugin.FileLister
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FileListerTest {

    @TempDir
    lateinit var tempDir: File

    @Test
    fun shouldListJarFilesFromDirectoryContainingOnlyJars() {
        // given
        val path = tempDir.toString()
        val file1 = File(tempDir, "plugin1.jar").apply { createNewFile() }
        val file2 = File(tempDir, "plugin2.jar").apply { createNewFile() }

        // when
        val result = FileLister.listJars(path)

        // then
        assertEquals(2, result?.size)
        assertTrue(result?.contains(file1) == true)
        assertTrue(result?.contains(file2) == true)
    }

    @Test
    fun shouldListOnlyJarFilesFromDirectoryContainingManyDifferentFiles() {
        // given
        val path = tempDir.toString()
        val file1 = File(tempDir, "plugin1.jar").apply { createNewFile() }
        val file2 = File(tempDir, "plugin2.jar").apply { createNewFile() }
        val file3 = File(tempDir, "a.jar").apply { createNewFile() }

        File(tempDir, "a.test").createNewFile()
        File(tempDir, "a.jars").createNewFile()
        File(tempDir, "3176231263681732817328.jur").createNewFile()
        File(tempDir, "----.txt").createNewFile()

        // when
        val result = FileLister.listJars(path)

        // then
        assertEquals(3, result?.size)
        assertTrue(result?.contains(file1) == true)
        assertTrue(result?.contains(file2) == true)
        assertTrue(result?.contains(file3) == true)
    }
}
