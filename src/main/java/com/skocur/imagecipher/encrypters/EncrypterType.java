package com.skocur.imagecipher.encrypters;

import com.skocur.imagecipher.encrypters.exceptions.InvalidEncryptionTypeException;
import org.jetbrains.annotations.NotNull;

public enum EncrypterType {
    SINGLE_LEVEL_ENCRYPTION,
    MULTI_COLOR_ENCRYPTION,
    LOW_LEVEL_BIT_ENCRYPTION,
    RSA_ENCRYPTION;

    @NotNull
    public static EncrypterType getType(int num) {
        if (num == 1) {
            return SINGLE_LEVEL_ENCRYPTION;
        } else if (num == 2) {
            return MULTI_COLOR_ENCRYPTION;
        } else if (num == 3) {
            return LOW_LEVEL_BIT_ENCRYPTION;
        } else if (num == 4) {
            return RSA_ENCRYPTION;
        } else {
            throw new InvalidEncryptionTypeException(
                    String.format("Invalid type: %d", num)
            );
        }
    }
}