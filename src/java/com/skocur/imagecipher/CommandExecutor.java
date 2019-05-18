package com.skocur.imagecipher;

import com.beust.jcommander.JCommander;
import com.skocur.imagecipher.encrypters.Encrypter;
import com.skocur.imagecipher.encrypters.LowLevelBitEncryption;
import com.skocur.imagecipher.encrypters.MultiColorEncryption;
import com.skocur.imagecipher.encrypters.SingleColorEncryption;

import java.io.IOException;
import java.util.Scanner;

public class CommandExecutor {

    public static void executeArgs(String[] args) {
        CommandArgs commandArgs = new CommandArgs();

        JCommander.newBuilder()
                .addObject(commandArgs)
                .build()
                .parse(args);

        if (commandArgs.encryptionMode > 0 && commandArgs.decryptionMode == 0) {
            try {
                encrypt(commandArgs);
            } catch (IOException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
        } else if (commandArgs.decryptionMode > 0 && commandArgs.encryptionMode == 0) {
            try {
                decrypt(commandArgs);
            } catch (IOException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
        } else {
            System.err.println("You cannot encrypt and decrypt data at the same time");
            System.exit(2);
        }
    }

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
            default:
                encrypter = new LowLevelBitEncryption(args.originalFileName);
                System.err.println("There is no available such encryption option!");
                System.err.println("LowLevelBitEncryption has been chosen by default");
        }

        Scanner scanner = new Scanner(System.in);

        System.out.println("Message to encrypt:\n\n");
        StringBuilder message = new StringBuilder();
        String text;
        while (!(text = scanner.nextLine()).equals(":exit")) {
            message.append(text);
        }

        encrypter.encrypt(message.toString());
    }

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
        }

        System.out.println(message);
    }
}
