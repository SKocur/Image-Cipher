package com.skocur.imagecipher;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

public class CommandArgs {

    @Parameter
    private List<String> parameters = new ArrayList<>();

    @Parameter(names = { "-M", "--mode"}, description = "Image-cipher mode, 1 is encryption, 2 is decryption")
    private int mode = 1;

    @Parameter(names = { "-f", "--file-name"}, description = "Path to file which will store encrypted data")
    private String originalFileName;

    @Parameter(names = { "-m", "--message"}, description = "Message which will be stored in image")
    private String message;
}
