package com.lypaka.bettercrates.Commands;

import com.lypaka.bettercrates.BetterCrates;
import com.lypaka.bettercrates.Crates.CrateGUI;
import com.lypaka.lypakautils.FancyText;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Map;

public class ClaimCommand {

    public ClaimCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterCratesCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(Commands.literal("claim")
                                    .executes(c -> {

                                        if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                            ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                            if (CrateGUI.playersAbleToClaimShitInTheEventTheyAccidentallyCloseTheFuckingMenuLikeAnIdiot.containsKey(player.getUniqueID())) {

                                                Map<ItemStack, List<String>> map = CrateGUI.playersAbleToClaimShitInTheEventTheyAccidentallyCloseTheFuckingMenuLikeAnIdiot.get(player.getUniqueID());
                                                for (Map.Entry<ItemStack, List<String>> entry : map.entrySet()) {

                                                    for (String command : entry.getValue()) {

                                                        player.world.getServer().getCommandManager().handleCommand(player.world.getServer().getCommandSource(), command.replace("%player%", player.getName().getString()));

                                                    }

                                                }
                                                CrateGUI.playersAbleToClaimShitInTheEventTheyAccidentallyCloseTheFuckingMenuLikeAnIdiot.entrySet().removeIf(entry -> entry.getKey().toString().equalsIgnoreCase(player.getUniqueID().toString()));

                                            } else {

                                                player.sendMessage(FancyText.getFormattedText("&eYou don't have a crate to claim!"), player.getUniqueID());

                                            }

                                        }

                                        return 1;

                                    })
                            )
            );

        }

    }

}
