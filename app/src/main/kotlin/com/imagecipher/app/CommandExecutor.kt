package com.imagecipher.app

import com.beust.jcommander.JCommander
import com.imagecipher.app.encrypters.EncrypterManager
import com.imagecipher.app.encrypters.EncrypterType
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.IOException
import java.util.*


object CommandExecutor {
    private val logger: Logger = LogManager.getLogger()

    @Throws(IOException::class)
    fun executeArgs(args: Array<String?>) {
        logger.info("Executing command line arguments passed to ImageCipher")
        val commandArgs = CommandArgs()

        JCommander.newBuilder()
            .addObject(commandArgs)
            .build()
            .parse(*args)

        if (commandArgs.encryptionMode > 0 && commandArgs.decryptionMode == 0) {
            encrypt(commandArgs)
        } else if (commandArgs.decryptionMode > 0 && commandArgs.encryptionMode == 0) {
            decrypt(commandArgs)
        } else if (commandArgs.encryptionMode > 0 && commandArgs.decryptionMode > 0) {
            System.err.println("You cannot encrypt and decrypt data at the same time")
            logger.error("Cannot encrypt and decrypt data at the same time")
            System.exit(2)
        }
    }

    /**
     * This method creates an Encryption object based on user preferences derived from command line.
     * By default (if user's option doesn't match anything) LowLevelBitEncryption is chosen. For now
     * user can only encrypt data that are fetched from console using simple Scanner. If user types
     * ":exit", loop which reads message will stop and encryption process will be invoked.
     *
     * @param args CommandArgs object that contains program arguments
     */
    private fun encrypt(args: CommandArgs) {
        logger.info("Encrypting data passed via command line interface")
        logger.debug("Encryption mode: " + args.encryptionMode)
        EncrypterManager.getEncrypter(
            EncrypterType.getType(args.encryptionMode), checkNotNull(args.originalFileName)
        ).use { encrypter ->
            println("Message to encrypt:\n\n")
            val message = StringBuilder()
            var text: String?
            val scanner = Scanner(System.`in`)
            while ((scanner.nextLine().also { text = it }) != ":exit") {
                message.append(text)
            }

            if (encrypter == null) {
                logger.error("Encrypter is null")
                println("Encrypter is null, maybe you provided invalid algorithm number?")
                return
            }
            encrypter.encrypt(message.toString())
        }
    }

    /**
     * Prints to console data that were retrieved from image. Decryption mode is chosen by user.
     *
     * @param args CommandArgs object that contains program arguments
     */
    @kotlin.Throws(IOException::class)
    private fun decrypt(args: CommandArgs) {
        logger.info("Decrypting data passed via command line interface")
        val fileName = checkNotNull(args.originalFileName)
        var message = ""

        when (args.decryptionMode) {
            1 -> message = Decrypter.decrypt(fileName)
            2 -> message = Decrypter.decryptBlue(fileName)
            3 -> {
                val decrypter = Decrypter(fileName)
                message = decrypter.decryptLowLevelBits()
            }

            else -> {
                logger.error("No valid decryption mode was chosen!")
                System.exit(2)
            }
        }

        println(message)
    }
}
