package org.example;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
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
        var results = countLatinLetters(inputFile);
        writeResultsToFile(results, outputFile);
    }

    private static String promptFilename(String fileDescription) {
        System.out.printf("Please, enter the %s filename: ", fileDescription);
        return scanner.nextLine();
    }

    /**
     * Returns a map in which keys are characters and values are numbers of occurrences
     * @param file A file to perform counting on
     * @return An aforementioned map
     * @throws IOException in cases if the file doesn't exist or stream was closed
     */
    private static Map<Character, Integer> countLatinLetters(File file) throws IOException {
        Map<Character, Integer> counts = new HashMap<>();
        try (var bis = new BufferedInputStream(new FileInputStream(file))) {
            int character;
            while ((character = bis.read()) != -1) {
                if (Character.UnicodeBlock.of(character).equals(Character.UnicodeBlock.BASIC_LATIN)
                    && Character.isAlphabetic(character)) {
                    if (Character.isLowerCase(character)) {
                        counts.put((char) character, counts.getOrDefault((char) character, 0) + 1);
                    } else {
                        counts.put((char) character, counts.getOrDefault((char) character, 0) + 1);
                    }
                }
            }
        }
        return counts;
    }

    private static void writeResultsToFile(Map<Character, Integer> results, File outputFile) throws IOException {
        try (var fw = new FileWriter(outputFile, false)) {
            fw.write("Counting results:" + System.lineSeparator());
            for (var entry : results.entrySet()) {
                fw.write(String.format("%c: %d%n", entry.getKey(), entry.getValue()));
            }
            fw.write(String.format("Total capital latin letters: %d%n", results.entrySet().stream()
                    .filter(e -> Character.isUpperCase(e.getKey()))
                    .mapToInt(Map.Entry::getValue)
                    .sum()));
            fw.write(String.format("Total lowercase latin letters: %d%n", results.entrySet().stream()
                    .filter(e -> Character.isLowerCase(e.getKey()))
                    .mapToInt(Map.Entry::getValue)
                    .sum()));
            fw.write(String.format("Total distinct capital latin letters: %d%n", results.keySet().stream()
                    .filter(Character::isUpperCase)
                    .distinct()
                    .count()));
            fw.write(String.format("Total distinct lowercase latin letters: %d%n", results.keySet().stream()
                    .filter(Character::isLowerCase)
                    .distinct()
                    .count()));
        }
    }
}