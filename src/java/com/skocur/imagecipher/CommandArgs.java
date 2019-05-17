package com.skocur.imagecipher;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

public class CommandArgs {

    @Parameter
    public List<String> parameters = new ArrayList<>();

    @Parameter(names = { "-EM", "--enryption-mode"}, description = "Encryption mode: 1 - SingleColorEncryption, 2 - MultiColorEncryption, 3 - LowLevelBitEncryption")
    public int encryptionMode = 0; // If 0, then message will not be encrypted

    @Parameter(names = { "-DM", "--decryption-mode"}, description = "Decryption mode")
    public int decryptionMode = 0; // If 0, then message will not be decrypted

    @Parameter(names = { "-f", "--file-name"}, description = "Path to file which will store encrypted data")
    public String originalFileName;

    @Parameter(names = { "-m", "--message"}, description = "Message which will be stored in image")
    public String message;
}
