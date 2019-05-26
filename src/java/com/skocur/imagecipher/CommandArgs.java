package com.skocur.imagecipher;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

public class CommandArgs {

    @Parameter
    public List<String> parameters = new ArrayList<>();

    @Parameter(names = { "-EM", "--enryption-mode"}, description = "Encryption mode: 1 - SingleColorEncryption, 2 - MultiColorEncryption, 3 - LowLevelBitEncryption, 4 - RSAEncryption")
    public int encryptionMode = 0; // If 0, then message will not be encrypted

    @Parameter(names = { "-DM", "--decryption-mode"}, description = "Decryption mode: options as in encryption mode")
    public int decryptionMode = 0; // If 0, then message will not be decrypted

    @Parameter(names = { "-f", "--file-name"}, description = "Path to file which will store encrypted data")
    public String originalFileName;

    @Parameter(names = { "-cf", "--certificate-file"}, description = "Path to file that contains certificate")
    public String certificateFileName;

    @Parameter(names = { "--noise-image"}, description = "Creates noise from image")
    public int imageNoise;
}