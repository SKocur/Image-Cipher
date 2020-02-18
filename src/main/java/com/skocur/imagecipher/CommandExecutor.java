package com.skocur.imagecipher;

import com.beust.jcommander.JCommander;
import com.skocur.imagecipher.encrypters.*;
import com.skocur.imagecipher.tools.imageprocessing.ColorFilter;
import com.skocur.imagecipher.tools.imageprocessing.FilteringColorMode;
import com.skocur.imagecipher.tools.imageprocessing.ImageNoise;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class CommandExecutor {

    public static void executeArgs(String[] args) throws IOException {
        CommandArgs commandArgs = new CommandArgs();

        JCommander.newBuilder()
                .addObject(commandArgs)
                .build()
                .parse(args);

        if (commandArgs.encryptionMode > 0 && commandArgs.decryptionMode == 0) {
            encrypt(commandArgs);
        } else if (commandArgs.decryptionMode > 0 && commandArgs.encryptionMode == 0) {
            decrypt(commandArgs);
        } else if (commandArgs.encryptionMode > 0 && commandArgs.decryptionMode > 0) {
            System.err.println("You cannot encrypt and decrypt data at the same time");
            System.exit(2);
        } else if (commandArgs.imageNoise > 0) {
            ImageNoise imageNoise = new ImageNoise(commandArgs.originalFileName);
            imageNoise.saveNoiseImage(imageNoise.createRandomNoise());
        } else if (commandArgs.imageFilterColor > 0) {
            FilteringColorMode colorMode = FilteringColorMode.RED;

            switch (commandArgs.imageFilterColor) {
                case 1:
                    colorMode = FilteringColorMode.BLUE;
                    break;
                case 2:
                    colorMode = FilteringColorMode.GREEN;
                    break;
            }

            ColorFilter.getColorAndSave(new File(commandArgs.originalFileName), colorMode);
        }
    }

    /**
     * This method creates an Encryption object based on user preferences
     * derived from command line. By default (if user's option doesn't match anything)
     * LowLevelBitEncryption is chosen. For now user can only encrypt data that are
     * fetched from console using simple Scanner. If user types ":exit", loop which
     * reads message will stop and encryption process will be invoked.
     *
     * @param args CommandArgs object that contains program arguments
     * @throws IOException
     */
    private static void encrypt(CommandArgs args) throws IOException {
        Encrypter encrypter;

        switch (args.encryptionMode) {
            case 1:
                encrypter = new SingleColorEncryption(args.originalFileName);
                break;
            case 2:
                encrypter = new MultiColorEncryption(args.originalFileName);
                break;
            case 3:
                encrypter = new LowLevelBitEncryption(args.originalFileName);
                break;
            case 4:
                encrypter = new RSAEncryption(args.originalFileName, args.certificateFileName);
                break;
            default:
                encrypter = new LowLevelBitEncryption(args.originalFileName);
                System.err.println("There is no available such encryption option!");
                System.err.println("LowLevelBitEncryption has been chosen by default");
        }

        System.out.println("Message to encrypt:\n\n");

        StringBuilder message = new StringBuilder();
        String text;
        Scanner scanner = new Scanner(System.in);
        while (!(text = scanner.nextLine()).equals(":exit")) {
            message.append(text);
        }

        encrypter.encrypt(message.toString());
    }

    /**
     * Prints to console data that were retrieved from image. Decryption mode
     * is chosen by user.
     *
     * @param args CommandArgs object that contains program arguments
     * @throws IOException
     */
    private static void decrypt(CommandArgs args) throws IOException {
        String message = "";

        switch (args.decryptionMode) {
            case 1:
                message = Decrypter.decrypt(args.originalFileName);
                break;
            case 2:
                message = Decrypter.decryptBlue(args.originalFileName);
                break;
            case 3:
                Decrypter decrypter = new Decrypter(args.originalFileName);
                message = decrypter.decryptLowLevelBits();
                break;
            case 4:
                // TODO: Create decrypter for RSAEncryption
                break;
            default:
                System.err.println("No valid decryption mode was chosen!");
                System.exit(2);
        }

        System.out.println(message);
    }
}
