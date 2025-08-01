package com.imagecipher.app.encrypters

import org.apache.logging.log4j.LogManager
import java.nio.charset.StandardCharsets
import java.security.interfaces.RSAPublicKey
import javax.crypto.Cipher

class RSAEncryption : Encrypter {

    private var certificateFileName: String?
    var pubkey: RSAPublicKey? = null

    companion object {
        private val logger = LogManager.getLogger()
    }

    constructor(fileName: String, certificateFileName: String?) : super(fileName) {
        this.certificateFileName = certificateFileName
    }

    constructor(fileName: String) : super(fileName) {
        this.certificateFileName = null
    }

    override fun encrypt(text: String) {
        logger.debug("Encrypting: $text")
        try {
            val encrypted: ByteArray = RSATypeEncryption(text)
            // Further encryption logic can be added here
        } catch (e: Exception) {
            logger.error("Encryption failed: ${e.message}", e)
        }
    }

    private fun RSATypeEncryption(text: String): ByteArray {
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.ENCRYPT_MODE, pubkey)
        return cipher.doFinal(text.toByteArray(StandardCharsets.UTF_8))
    }
}
