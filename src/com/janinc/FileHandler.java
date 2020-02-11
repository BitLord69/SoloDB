package com.janinc;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-10
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHandler {
    public static void writeFile(String directory, String fileName, Object object) {
        if (!directory.isBlank()) {
            File folder = new File("./" + directory);
            if (!folder.exists()){
                folder.mkdir();
            } // if !folder...

            directory = "/" + directory;
        } // if !directory...

        Path path = Paths.get(directory + fileName);

        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path))) {
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        } // catch
    } // readFile

    public static Object readFile(String directory, String fileName) {
        Path path = Paths.get((directory.isBlank() ? "" : directory + "/") + fileName);

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } // catch
        return null;
    } // readFile
} // class FileHandler