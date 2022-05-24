package com.plasmidEditor.sputnik.utils;

import com.plasmidEditor.sputnik.exceptions.FileReadingException;

import java.io.*;

public class ReaderUtils {
    public static String readStringFromFile(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append("\n").append(line);
                line = br.readLine();
            }
            return sb.toString();
        } catch (IOException e) {
            throw new FileReadingException(path, e);
        }
    }
}
