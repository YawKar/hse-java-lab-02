package org.example;

import java.io.*;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            run();
        } catch (Exception e) {
            System.out.printf("Caught an exception during execution: %s%n", e.getMessage());
        }
    }

    public static void run() throws IOException {
        var inputFilename = promptFilename("input");
        var inputFile = new File(inputFilename);
        if (!inputFile.exists()) {
            throw new FileNotFoundException(String.format("File '%s' doesn't exist", inputFile.getAbsolutePath()));
        }
        var outputFilename = promptFilename("output");
        var outputFile = new File(outputFilename);
        if (outputFile.createNewFile()) {
            System.out.printf("File '%s' doesn't exist, created the empty one%n", outputFile.getAbsolutePath());
        }
        var results = countUpperAndLowerCharacters(inputFile);
        writeResultsToFile(results[0], results[1], outputFile);
    }

    private static String promptFilename(String fileDescription) {
        System.out.print(String.format("Please, enter the %s filename: ", fileDescription));
        return scanner.nextLine();
    }

    /**
     * Counts lower- and upper-cased latin characters
     * @param file
     * @return array with 2 numbers: [lower, upper]
     * @throws IOException in cases if the file doesn't exist or stream was closed
     */
    private static int[] countUpperAndLowerCharacters(File file) throws IOException {
        int[] counts = {0, 0};
        try (var bis = new BufferedInputStream(new FileInputStream(file))) {
            int character;
            while ((character = bis.read()) != -1) {
                if (Character.UnicodeBlock.of(character).equals(Character.UnicodeBlock.BASIC_LATIN)
                    && Character.isAlphabetic(character)) {
                    if (Character.isLowerCase(character)) {
                        ++counts[0];
                    } else {
                        ++counts[1];
                    }
                }
            }
        }
        return counts;
    }

    private static void writeResultsToFile(int lowerCased, int upperCased, File outputFile) throws IOException {
        try (var fw = new FileWriter(outputFile, false)) {
            fw.write("Counting results:" + System.lineSeparator());
            fw.write(String.format("Lower-cased: %d%n", lowerCased));
            fw.write(String.format("Upper-cased: %d%n", upperCased));
        }
    }
}