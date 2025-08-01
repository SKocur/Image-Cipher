package com.imagecipher.app

import com.beust.jcommander.JCommander
import com.beust.jcommander.ParameterException
import com.imagecipher.app.encrypters.EncrypterManager
import com.imagecipher.app.encrypters.EncrypterType
import com.imagecipher.app.tools.ManifestReader
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.system.exitProcess


object CommandExecutor {

    @Throws(IOException::class)
    fun executeArgs(args: Array<String?>) {
        val commandArgs = CommandArgs()
        val jCommander = JCommander.newBuilder()
            .addObject(commandArgs)
            .programName("image-cipher")
            .build()

        try {
            jCommander.parse(*args)
        } catch (e: ParameterException) {
            System.err.println("Error: ${e.message}")
            System.err.println("Use --help for usage information")
            exitProcess(1)
        }

        when {
            commandArgs.help -> {
                showHelp(jCommander)
                return
            }

            commandArgs.version -> {
                showVersion()
                return
            }

            commandArgs.encryptAlgorithm != null && commandArgs.decryptAlgorithm != null -> {
                System.err.println("Error: Cannot encrypt and decrypt simultaneously")
                System.err.println("Use either --encrypt or --decrypt, not both")
                exitProcess(2)
            }

            commandArgs.encryptAlgorithm != null -> {
                if (commandArgs.imageFile == null) {
                    System.err.println("Error: Input image file is required for encryption")
                    System.err.println("Use --input <image_file>")
                    exitProcess(1)
                }
                encrypt(commandArgs)
            }

            commandArgs.decryptAlgorithm != null -> {
                if (commandArgs.imageFile == null) {
                    System.err.println("Error: Input image file is required for decryption")
                    System.err.println("Use --input <image_file>")
                    exitProcess(1)
                }
                decrypt(commandArgs)
            }

            else -> {
                System.err.println("Error: No operation specified")
                System.err.println("Use --encrypt or --decrypt")
                System.err.println("Use --help for usage information")
                exitProcess(1)
            }
        }
    }

    private fun showHelp(jCommander: JCommander) {
        println("Image Cipher - Steganography tool for hiding text in images")
        println("=".repeat(60))
        jCommander.usage()
        println()
        println("Examples:")
        println("  Encrypt text into image:")
        println("    image-cipher --encrypt single-color --input photo.jpg --text \"Secret message\"")
        println("    image-cipher -e multi-color -i image.png -t \"Hello World\" -o encrypted.png")
        println()
        println("  Decrypt text from image:")
        println("    image-cipher --decrypt single-color --input encrypted.jpg")
        println("    image-cipher -d low-level-bit -i hidden_data.png")
        println()
        println("  Interactive encryption (read from stdin):")
        println("    image-cipher -e single-color -i photo.jpg -t :stdin")
        println()
        println("Supported algorithms:")
        println("  single-color    - Basic LSB steganography in single color channel")
        println("  multi-color     - Enhanced LSB using multiple color channels")
        println("  low-level-bit   - Advanced bit-level manipulation")
        println("  rsa            - RSA encryption with steganography (experimental)")
    }

    private fun showVersion() {
        try {
            val buildTime = ManifestReader.getBuildTime()
            println("Image Cipher version: 1.0.0")
            if (buildTime != null) {
                println("Built: $buildTime")
            } else {
                println("Built: development")
            }
        } catch (e: Exception) {
            println("Image Cipher version: 1.0.0 (development)")
        }
    }

    private fun validateFile(filePath: String, operation: String): Boolean {
        val file = File(filePath)
        if (!file.exists()) {
            System.err.println("Error: File '$filePath' does not exist")
            return false
        }
        if (!file.canRead()) {
            System.err.println("Error: Cannot read file '$filePath'")
            return false
        }
        if (operation == "encrypt" && !file.canWrite()) {
            System.err.println("Warning: Cannot write to file '$filePath'")
        }
        return true
    }

    private fun encrypt(args: CommandArgs) {
        if (!validateFile(checkNotNull(args.imageFile), "encrypt")) {
            return
        }

        if (args.verbose) {
            println("Encrypting data using ${args.encryptAlgorithm} algorithm")
            println("Input image: ${args.imageFile}")
            args.outputFile?.let { println("Output image: $it") }
        }

        val encrypter = EncrypterManager.getEncrypter(
            EncrypterType.getType(args.encryptionMode),
            checkNotNull(args.imageFile)
        )

        if (encrypter == null) {
            System.err.println("Error: Invalid encryption algorithm '${args.encryptAlgorithm}'")
            System.err.println("Supported algorithms: single-color, multi-color, low-level-bit, rsa")
            exitProcess(1)
        }

        try {
            encrypter.use { enc ->
                val message = when {
                    args.textToEncrypt == ":stdin" || args.textToEncrypt == null -> {
                        println("Enter text to encrypt (type :exit on new line to finish):")
                        val text = StringBuilder()
                        val scanner = Scanner(System.`in`)
                        var line: String?
                        while ((scanner.nextLine().also { line = it }) != ":exit") {
                            if (text.isNotEmpty()) text.append("\n")
                            text.append(line)
                        }
                        text.toString()
                    }

                    else -> args.textToEncrypt!!
                }

                if (message.isEmpty()) {
                    System.err.println("Error: No text provided for encryption")
                    exitProcess(1)
                }

                enc.encrypt(message)

                if (args.verbose) {
                    println("Encryption completed successfully")
                    println("Text length: ${message.length} characters")
                }
            }
        } catch (e: Exception) {
            System.err.println("Error during encryption: ${e.message}")
            if (args.verbose) {
                e.printStackTrace()
            }
            exitProcess(1)
        }
    }

    @Throws(IOException::class)
    private fun decrypt(args: CommandArgs) {
        if (!validateFile(checkNotNull(args.imageFile), "decrypt")) {
            return
        }

        if (args.verbose) {
            println("Decrypting data using ${args.decryptAlgorithm} algorithm")
            println("Input image: ${args.imageFile}")
        }

        val fileName = checkNotNull(args.imageFile)

        try {
            val message = when (args.decryptionMode) {
                1 -> Decrypter.decrypt(fileName)
                2 -> Decrypter.decryptBlue(fileName)
                3 -> {
                    val decrypter = Decrypter(fileName)
                    decrypter.decryptLowLevelBits()
                }

                else -> {
                    System.err.println("Error: Invalid decryption algorithm '${args.decryptAlgorithm}'")
                    System.err.println("Supported algorithms: single-color, multi-color, low-level-bit")
                    exitProcess(2)
                }
            }

            if (args.verbose) {
                println("Decryption completed successfully")
                println("Message length: ${message.length} characters")
                println("--- Decrypted Message ---")
            }

            println(message)

        } catch (e: Exception) {
            System.err.println("Error during decryption: ${e.message}")
            if (args.verbose) {
                e.printStackTrace()
            }
            exitProcess(1)
        }
    }
}
