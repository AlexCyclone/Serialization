package com.devianta;

import java.io.*;

public interface FileOperation {

    static void stringWrite(String str, File file) {
        if (file == null || str == null) {
            throw new IllegalArgumentException("Null pointer found");
        }

        if (file.exists() && file.isDirectory()) {
            throw new IllegalArgumentException("Directory found, file expected");
        }

        try (PrintWriter pw = new PrintWriter(file)) {
            pw.print(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String stringRead(File file) {
        if (file == null) {
            throw new IllegalArgumentException("Null pointer found");
        }

        if (!file.exists() || file.isDirectory()) {
            throw new IllegalArgumentException("File not found");
        }

        StringBuffer sb = new StringBuffer();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
