package com.lypaka.bettercrates.Commands;

import com.lypaka.bettercrates.BetterCrates;
import com.lypaka.bettercrates.ConfigGetters;
import com.lypaka.bettercrates.Crates.Crate;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.ArrayList;
import java.util.HashMap;

public class ReloadCommand {

    public ReloadCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterCratesCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(Commands.literal("reload")
                                    .executes(c -> {

                                        if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                            ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                            if (!PermissionHandler.hasPermission(player, "bettercrates.command.admin")) {

                                                player.sendMessage(FancyText.getFormattedText("&cYou do not have permission to use this command!"), player.getUniqueID());
                                                return 0;

                                            }

                                        }

                                        try {

                                            BetterCrates.configManager.load();
                                            ConfigGetters.load();
                                            BetterCrates.crateConfigManager.setFileNames(ConfigGetters.crateFiles);
                                            BetterCrates.crateConfigManager.load();
                                            BetterCrates.crates = new ArrayList<>();
                                            BetterCrates.crateMap = new HashMap<>();
                                            for (int i = 0; i < ConfigGetters.crateFiles.size(); i++) {

                                                Crate crate = new Crate(i);
                                                crate.load();

                                            }

                                        } catch (ObjectMappingException e) {

                                            e.printStackTrace();

                                        }

                                        c.getSource().sendFeedback(FancyText.getFormattedText("&aSuccessfully reloaded BetterCrates!"), true);
                                        return 1;
                                    })
                            )
            );

        }

    }

}
