package com.lypaka.bettercrates;

import com.lypaka.bettercrates.Crates.Crate;
import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;
import com.lypaka.lypakautils.ConfigurationLoaders.ComplexConfigManager;
import com.lypaka.lypakautils.ConfigurationLoaders.ConfigUtils;
import net.minecraftforge.fml.common.Mod;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("bettercrates")
public class BetterCrates {

    public static final String MOD_ID = "bettercrates";
    public static final String MOD_NAME = "BetterCrates";
    private static final Logger logger = LogManager.getLogger();
    public static BasicConfigManager configManager;
    public static ComplexConfigManager crateConfigManager;
    public static List<Crate> crates = new ArrayList<>();
    public static Map<String, Crate> crateMap = new HashMap<>();
    public static Random random = new Random();

    public BetterCrates() throws IOException, ObjectMappingException {

        Path dir = ConfigUtils.checkDir(Paths.get("./config/bettercrates"));
        String[] files = new String[]{"bettercrates.conf", "crateExample.conf", "storage.conf"};
        configManager = new BasicConfigManager(files, dir, BetterCrates.class, MOD_NAME, MOD_ID, logger);
        configManager.init();
        ConfigGetters.load();
        crateConfigManager = new ComplexConfigManager(ConfigGetters.crateFiles, "crates", "blank-file.conf", dir, BetterCrates.class, MOD_NAME, MOD_ID, logger);
        crateConfigManager.init();

    }

}
