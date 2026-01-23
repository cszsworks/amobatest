package com.cszsworks.saves;

import java.io.*;

public class SaveManager {

    public static void createSave(GameSaveData save, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(save);
            System.out.println("Game saved to " + filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static GameSaveData loadSave(String filename) {
        File file = new File(filename);

        if (!file.exists()) {
            System.out.println("Save file not found: " + filename);
            return null; // nincs mit tölteni
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (GameSaveData) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to load save: " + filename);
            e.printStackTrace();
            return null;
        }
    }
}
