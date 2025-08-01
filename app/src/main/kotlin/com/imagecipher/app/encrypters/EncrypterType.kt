package com.imagecipher.app.encrypters

import com.imagecipher.app.encrypters.exceptions.InvalidEncryptionTypeException

enum class EncrypterType {
    SINGLE_LEVEL_ENCRYPTION,
    MULTI_COLOR_ENCRYPTION,
    LOW_LEVEL_BIT_ENCRYPTION,
    RSA_ENCRYPTION;

    companion object {
        fun getType(num: Int): EncrypterType {
            return when (num) {
                1 -> SINGLE_LEVEL_ENCRYPTION
                2 -> MULTI_COLOR_ENCRYPTION
                3 -> LOW_LEVEL_BIT_ENCRYPTION
                4 -> RSA_ENCRYPTION
                else -> throw InvalidEncryptionTypeException("Invalid type: $num")
            }
        }
    }
}
