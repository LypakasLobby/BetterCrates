package com.lypaka.bettercrates.Listeners;

import com.lypaka.bettercrates.BetterCrates;
import com.lypaka.bettercrates.Commands.BetterCratesCommand;
import com.lypaka.bettercrates.ConfigGetters;
import com.lypaka.bettercrates.Crates.Crate;
import com.lypaka.lypakautils.ModVerification;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

@Mod.EventBusSubscriber(modid = BetterCrates.MOD_ID)
public class ServerStartingListener {

    @SubscribeEvent
    public static void onServerStarting (FMLServerStartingEvent event) throws ObjectMappingException {

        for (int i = 0; i < ConfigGetters.crateFiles.size(); i++) {

            Crate crate = new Crate(i);
            crate.load();

        }

        MinecraftForge.EVENT_BUS.register(new BlockInteractListener());
        MinecraftForge.EVENT_BUS.register(new EntityInteractListener());

    }

}
