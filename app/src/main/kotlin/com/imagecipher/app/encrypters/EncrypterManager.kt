package com.imagecipher.app.encrypters

import org.apache.logging.log4j.LogManager
import java.io.IOException

object EncrypterManager {

    private val logger = LogManager.getLogger()

    fun getEncrypter(type: EncrypterType, fileName: String): Encrypter? {
        return try {
            when (type) {
                EncrypterType.SINGLE_LEVEL_ENCRYPTION -> SingleColorEncryption(fileName)
                EncrypterType.MULTI_COLOR_ENCRYPTION -> MultiColorEncryption(fileName)
                EncrypterType.LOW_LEVEL_BIT_ENCRYPTION -> LowLevelBitEncryption(fileName)
                EncrypterType.RSA_ENCRYPTION -> RSAEncryption(fileName)
            }
        } catch (e: IOException) {
            logger.error("Cannot create encryption instance: ${e.message}")
            null
        }
    }
}
