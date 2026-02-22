package dev.tamago0314.astmoss.client.Config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ConfigManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File FILE = new File("config/astmoss.json");

    public static AstmossConfig config = new AstmossConfig();

    public static void load() {
        try {
            if (FILE.exists()) {
                FileReader reader = new FileReader(FILE);
                config = GSON.fromJson(reader, AstmossConfig.class);
                reader.close();
            } else {
                save();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            FileWriter writer = new FileWriter(FILE);
            GSON.toJson(config, writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}