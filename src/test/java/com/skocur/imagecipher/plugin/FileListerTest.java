package com.skocur.imagecipher.plugin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class FileListerTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldListJarFilesFromDirectoryContainingOnlyJars() throws IOException {
        // given
        String path = tempDir.toString();
        File file1 = new File(tempDir.toFile(), "plugin1.jar");
        File file2 = new File(tempDir.toFile(), "plugin2.jar");
        file1.createNewFile();
        file2.createNewFile();

        // when
        File[] result = FileLister.listJars(path);

        // then
        assertEquals(2, result.length);
        assertTrue(Arrays.asList(result).contains(file1));
        assertTrue(Arrays.asList(result).contains(file2));
    }

    @Test
    void shouldListOnlyJarFilesFromDirectoryContainingManyDifferentFiles() throws IOException {
        // given
        String path = tempDir.toString();
        File file1 = new File(tempDir.toFile(), "plugin1.jar");
        File file2 = new File(tempDir.toFile(), "plugin2.jar");
        File file3 = new File(tempDir.toFile(), "a.jar");
        file1.createNewFile();
        file2.createNewFile();
        file3.createNewFile();

        new File(tempDir.toFile(), "a.test").createNewFile();
        new File(tempDir.toFile(), "a.jars").createNewFile();
        new File(tempDir.toFile(), "3176231263681732817328.jur").createNewFile();
        new File(tempDir.toFile(), "----.txt").createNewFile();

        // when
        File[] result = FileLister.listJars(path);

        // then
        assertEquals(3, result.length);
        assertTrue(Arrays.asList(result).contains(file1));
        assertTrue(Arrays.asList(result).contains(file2));
        assertTrue(Arrays.asList(result).contains(file3));
    }

    @Test
    void shouldListNoJarsFromDirectoryContainingNoJars() throws IOException {
        // given
        String path = tempDir.toString();
        new File(tempDir.toFile(), "123123132.jarr").createNewFile();
        new File(tempDir.toFile(), "AAAAAA").createNewFile();
        new File(tempDir.toFile(), "testjar.txt").createNewFile();
        new File(tempDir.toFile(), "jarfile.file").createNewFile();

        // when
        File[] result = FileLister.listJars(path);

        // then
        assertEquals(0, result.length);
    }

    @Test
    void shouldCorrectlyListJarsUris() throws IOException {
        // given
        File file1 = new File(tempDir.toFile(), "plugin1.jar");
        File file2 = new File(tempDir.toFile(), "plugin2.jar");
        file1.createNewFile();
        file2.createNewFile();

        // when
        var result = FileLister.listUris(new File[]{file1, file2});

        // then
        assertEquals(2, result.size());
        assertTrue(result.contains(file1.toURI()));
        assertTrue(result.contains(file2.toURI()));
    }

    @Test
    void shouldReturnNoUriWhenFileArrayIsEmpty() {
        // when
        var result = FileLister.listUris(new File[0]);

        // then
        assertTrue(result.isEmpty());
    }
}
