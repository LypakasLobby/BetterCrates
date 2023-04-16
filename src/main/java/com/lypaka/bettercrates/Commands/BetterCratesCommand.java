package com.lypaka.bettercrates.Commands;

import com.lypaka.bettercrates.BetterCrates;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

import java.util.Arrays;
import java.util.List;

/**
 * FUCK Brigadier
 */
@Mod.EventBusSubscriber(modid = BetterCrates.MOD_ID)
public class BetterCratesCommand {

    public static List<String> ALIASES = Arrays.asList("bettercrates", "crates", "bcrates");

    @SubscribeEvent
    public static void onCommandRegistration (RegisterCommandsEvent event) {

        new ClaimCommand(event.getDispatcher());
        new KeyCommand(event.getDispatcher());
        new PreviewCommand(event.getDispatcher());
        new ReloadCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());

    }

}
