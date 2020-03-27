package com.skocur.imagecipher.plugin

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class FileListerTest extends Specification {

    @Rule
    TemporaryFolder testFolder = new TemporaryFolder()

    def "list jar files from directory that contains only jars"() {
        given:
        String path = testFolder.getRoot().getPath()
        File file1 = testFolder.newFile("plugin1.jar")
        File file2 = testFolder.newFile("plugin2.jar")

        expect:
        FileLister.listJars(path) == [file1, file2] as File[]
    }

    def "list only jar files from directory that contains many different files"() {
        given:
        String path = testFolder.getRoot().getPath()
        File file1 = testFolder.newFile("plugin1.jar")
        File file2 = testFolder.newFile("plugin2.jar")
        File file3 = testFolder.newFile("a.jar")

        testFolder.newFile("a.test")
        testFolder.newFile("a.jars")
        testFolder.newFile("3176231263681732817328.jur")
        testFolder.newFile("----.txt")

        expect:
        FileLister.listJars(path).sort() == [file3, file1, file2].sort() as File[]
    }

    def "list no jars from directory that contains no jars"() {
        given:
        String path = testFolder.getRoot().getPath()
        testFolder.newFile("123123132.jarr")
        testFolder.newFile("AAAAAA")
        testFolder.newFile("testjar.txt")
        testFolder.newFile("jarfile.file")

        expect:
        FileLister.listJars(path) == [] as File[]
    }

    def "correctly list jars uris"() {
        given:
        File file1 = testFolder.newFile("plugin1.jar")
        File file2 = testFolder.newFile("plugin2.jar")

        expect:
        FileLister.listUris([file1, file2] as File[]) == [file1.toURI(), file2.toURI()]
    }

    def "return no URI when file array is empty"() {
        expect:
        FileLister.listUris([] as File[]) == []
    }
}
