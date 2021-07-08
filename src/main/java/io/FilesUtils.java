package io;

import java.io.*;

public class FilesUtils {

    public static void main(String[] args) {
        File file = new File("src/main/resources/io/files/img.png");
        System.out.println(file.exists());
        File copy = new File("src/main/resources/io/files/copy.png");
        System.out.println(copy.exists());
        byte[] buffer = new byte[256];
        try (FileInputStream is = new FileInputStream(file);
             FileOutputStream os = new FileOutputStream(copy)) {
            int read;
            while ((read = is.read(buffer)) != -1) {
                os.write(buffer, 0, read);
                os.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
