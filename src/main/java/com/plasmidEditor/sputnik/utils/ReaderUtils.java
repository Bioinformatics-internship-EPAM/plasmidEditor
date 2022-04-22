package com.plasmidEditor.sputnik.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReaderUtils {
    public static String readStringFromFile(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
