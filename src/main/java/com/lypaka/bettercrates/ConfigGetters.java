package com.lypaka.bettercrates;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.List;
import java.util.Map;

public class ConfigGetters {

    public static List<String> crateFiles;
    public static Map<String, Map<String, String>> crateCooldowns;

    public static void load() throws ObjectMappingException {

        crateFiles = BetterCrates.configManager.getConfigNode(0, "Crates").getList(TypeToken.of(String.class));
        crateCooldowns = BetterCrates.configManager.getConfigNode(2, "Crate-Cooldowns").getValue(new TypeToken<Map<String, Map<String, String>>>() {});

    }

}
