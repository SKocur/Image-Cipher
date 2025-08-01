package com.imagecipher.app

import com.beust.jcommander.Parameter

class CommandArgs {

    @Parameter
    var parameters: MutableList<String> = mutableListOf()

    @Parameter(
        names = ["-EM", "--enryption-mode"],
        description = "Encryption mode: 1 - SingleColorEncryption, 2 - MultiColorEncryption, 3 - LowLevelBitEncryption, 4 - RSAEncryption"
    )
    var encryptionMode: Int = 0 // If 0, then message will not be encrypted

    @Parameter(
        names = ["-DM", "--decryption-mode"],
        description = "Decryption mode: options as in encryption mode"
    )
    var decryptionMode: Int = 0 // If 0, then message will not be decrypted

    @Parameter(
        names = ["-f", "--file-name"],
        description = "Path to file which will store encrypted data"
    )
    var originalFileName: String? = null

    @Parameter(
        names = ["-cf", "--certificate-file"],
        description = "Path to file that contains certificate"
    )
    var certificateFileName: String? = null
}
