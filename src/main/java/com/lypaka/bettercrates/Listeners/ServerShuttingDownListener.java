package com.lypaka.bettercrates.Listeners;

import com.lypaka.bettercrates.BetterCrates;
import com.lypaka.bettercrates.ConfigGetters;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;

@Mod.EventBusSubscriber(modid = BetterCrates.MOD_ID)
public class ServerShuttingDownListener {

    @SubscribeEvent
    public static void onServerShuttingDown (FMLServerStoppingEvent event) {

        BetterCrates.configManager.getConfigNode(2, "Crate-Cooldowns").setValue(ConfigGetters.crateCooldowns);

    }

}
