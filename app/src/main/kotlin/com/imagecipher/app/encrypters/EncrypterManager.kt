package com.imagecipher.app.encrypters

import java.io.IOException

object EncrypterManager {

    fun getEncrypter(type: EncrypterType, fileName: String): Encrypter? {
        return try {
            when (type) {
                EncrypterType.SINGLE_LEVEL_ENCRYPTION -> SingleColorEncryption(fileName)
                EncrypterType.MULTI_COLOR_ENCRYPTION -> MultiColorEncryption(fileName)
                EncrypterType.LOW_LEVEL_BIT_ENCRYPTION -> LowLevelBitEncryption(fileName)
                EncrypterType.RSA_ENCRYPTION -> RSAEncryption(fileName)
            }
        } catch (e: IOException) {
            System.err.println("Cannot create encryption instance: ${e.message}")
            null
        }
    }
}
