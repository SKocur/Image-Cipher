package com.imagecipher.app

import com.beust.jcommander.Parameter

class CommandArgs {

    @Parameter
    var parameters: MutableList<String> = mutableListOf()

    @Parameter(
        names = ["-h", "--help"],
        description = "Show this help message",
        help = true
    )
    var help: Boolean = false

    @Parameter(
        names = ["-e", "--encrypt"],
        description = "Encryption algorithm: single-color, multi-color, low-level-bit, rsa"
    )
    var encryptAlgorithm: String? = null

    @Parameter(
        names = ["-d", "--decrypt"],
        description = "Decryption algorithm: single-color, multi-color, low-level-bit"
    )
    var decryptAlgorithm: String? = null

    @Parameter(
        names = ["-i", "--input", "--image"],
        description = "Path to input image file",
        required = false
    )
    var imageFile: String? = null

    @Parameter(
        names = ["-o", "--output"],
        description = "Path to output image file (for encryption only)"
    )
    var outputFile: String? = null

    @Parameter(
        names = ["-t", "--text"],
        description = "Text to encrypt (use :stdin to read from standard input)"
    )
    var textToEncrypt: String? = null

    @Parameter(
        names = ["-c", "--certificate"],
        description = "Path to certificate file (for RSA encryption only)"
    )
    var certificateFile: String? = null

    @Parameter(
        names = ["-v", "--verbose"],
        description = "Enable verbose output"
    )
    var verbose: Boolean = false

    @Parameter(
        names = ["--version"],
        description = "Show version information"
    )
    var version: Boolean = false

    // Legacy support - keep old parameters but mark as deprecated
    val encryptionMode: Int
        get() = when (encryptAlgorithm?.lowercase()) {
            "single-color" -> 1
            "multi-color" -> 2
            "low-level-bit" -> 3
            "rsa" -> 4
            else -> 0
        }

    val decryptionMode: Int
        get() = when (decryptAlgorithm?.lowercase()) {
            "single-color" -> 1
            "multi-color" -> 2
            "low-level-bit" -> 3
            else -> 0
        }
}
