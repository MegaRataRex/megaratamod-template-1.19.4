package com.megarata.server;

import com.google.gson.JsonObject;
import com.megarata.config.Config;
import net.minecraft.resource.ResourceManager;

public class LootTableCache {
    private static JsonObject cachedJson;

    public static JsonObject getCachedJson() {
        return cachedJson;
    }

    public static void load(){
        cachedJson = Config.loadConfigFile();
    }
}
