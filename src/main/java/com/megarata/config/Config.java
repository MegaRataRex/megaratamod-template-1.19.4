package com.megarata.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    private static final String DEFAULT_IDENTIFIER_CONTENT = """
            {
              "minecraft:desert": ["minecraft:chests/desert_pyramid",
                "minecraft:chests/buried_treasure"],
              "minecraft:plains": ["minecraft:chests/simple_dungeon","minecraft:chests/bastion_treasure"],
              "minecraft:badlands": ["minecraft:chests/abandoned_mineshaft"],
              "default": ["minecraft:chests/ruined_portal"]
            }""";

    public static Path getFilePath(){
        return FabricLoader.getInstance().getConfigDir().resolve("megaratamod/identifiers.json");
    }


    //se asegura de crear una file de config, en caso de que no exista, o simplemente se asegura su existencia.
    public static void ensureConfigFileExists() {
        Path configFilePath = getFilePath();

        try {
            Files.createDirectories(configFilePath.getParent());
            if (!Files.exists(configFilePath)) {
                Files.writeString(configFilePath, DEFAULT_IDENTIFIER_CONTENT);
            }
        } catch (Exception e) {
        }
    }


    //método para refrescar el json.
    public static JsonObject loadConfigFile() {
        Path configFilePath = getFilePath();
        try {
            if (Files.exists(configFilePath)) {
                String jsonString = Files.readString(configFilePath);
                return JsonParser.parseString(jsonString).getAsJsonObject();
            }
        } catch (Exception e) {
        }
        return null;
    }
}
