package com.urlshortener.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UrlIdGenerationService {
    private final char[] symbols = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E',
            'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    @Value("${app.idLength}")
    private int idLength;

    private final Random randomizer = new Random();

    public String generateUrl() {
        StringBuilder urlId = new StringBuilder();
        for (int i = 0; i < idLength; i++) {
            int randomNumber = randomizer.nextInt(0, symbols.length);
            char randomSymbol = symbols[randomNumber];
            urlId.append(randomSymbol);
        }
        return urlId.toString();
    }
}
