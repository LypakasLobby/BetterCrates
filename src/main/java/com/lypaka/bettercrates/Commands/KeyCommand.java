package com.lypaka.bettercrates.Commands;

import com.lypaka.bettercrates.BetterCrates;
import com.lypaka.bettercrates.Crates.Crate;
import com.lypaka.bettercrates.Crates.CrateKey;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.PermissionHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

public class KeyCommand {

    public KeyCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterCratesCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal("bettercrates")
                            .then(Commands.literal("key")
                                    .then(Commands.literal("give")
                                            .then(Commands.argument("player", EntityArgument.players())
                                                    .then(Commands.argument("crate", StringArgumentType.string())
                                                            .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                                                    .executes(c -> {

                                                                        if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                                            ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                                            if (!PermissionHandler.hasPermission(player, "bettercrates.command.admin")) {

                                                                                player.sendMessage(FancyText.getFormattedText("&cYou do not have permission to use this command!"), player.getUniqueID());
                                                                                return 0;

                                                                            }

                                                                        }

                                                                        ServerPlayerEntity target = EntityArgument.getPlayer(c, "player");
                                                                        String crateName = StringArgumentType.getString(c, "crate");
                                                                        int amount = IntegerArgumentType.getInteger(c, "amount");

                                                                        Crate crate = Crate.getCrateFromName(crateName);
                                                                        if (crate == null) {

                                                                            c.getSource().sendErrorMessage(FancyText.getFormattedText("&eInvalid crate name!"));
                                                                            return 0;

                                                                        }

                                                                        CrateKey key = crate.getCrateKey();
                                                                        ItemStack item = key.getKey();
                                                                        item.setCount(amount);
                                                                        if (target.addItemStackToInventory(item)) {

                                                                            target.sendMessage(FancyText.getFormattedText("&aYou've been given " + amount + " keys to the " + crate.getCrateName() + " Crate!"), target.getUniqueID());
                                                                            c.getSource().sendFeedback(FancyText.getFormattedText("&eSuccessfully gave " + target.getName().getString() + " " + amount + " keys to the " + crate.getCrateName() + " Crate!"), true);

                                                                        }

                                                                        return 1;

                                                                    })
                                                            )
                                                    )
                                            )
                                    )
                            )
            );

        }

    }

}
