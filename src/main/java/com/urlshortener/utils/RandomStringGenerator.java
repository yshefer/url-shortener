package com.urlshortener.utils;

import lombok.experimental.UtilityClass;

import java.util.Random;

/**
 * Utility class for generating random strings.
 *
 * <p>This class provides a method to generate random strings of a specified length using a pool of alphanumeric characters.</p>
 *
 */
@UtilityClass
public class RandomStringGenerator {
    private final char[] symbols = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E',
            'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private final Random randomizer = new Random();

    /**
     * Generates a random string of the specified length using a pool of alphanumeric characters.
     *
     * @param stringLength the length of the random string to generate.
     * @return a random string of the specified length.
     */
    public String generateString(int stringLength) {
        StringBuilder generatedString = new StringBuilder();
        for (int i = 0; i < stringLength; i++) {
            int randomNumber = randomizer.nextInt(0, symbols.length);
            char randomSymbol = symbols[randomNumber];
            generatedString.append(randomSymbol);
        }
        return generatedString.toString();
    }
}
