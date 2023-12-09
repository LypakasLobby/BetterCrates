package com.lypaka.bettercrates.Commands;

import com.google.common.reflect.TypeToken;
import com.lypaka.bettercrates.BetterCrates;
import com.lypaka.bettercrates.Crates.Crate;
import com.lypaka.bettercrates.Crates.PreviewGUI;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.ItemStackBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBuilder;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.registries.PixelmonItems;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PreviewCommand {

    public PreviewCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterCratesCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(Commands.literal("preview")
                                    .then(Commands.argument("crateName", StringArgumentType.string())
                                            .suggests(
                                                    (context, builder) -> ISuggestionProvider.suggest(
                                                            BetterCrates.crateMap.keySet(), builder)
                                            )
                                            .executes(c -> {

                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                    String crateName = StringArgumentType.getString(c, "crateName");
                                                    Crate crate = Crate.getCrateFromName(crateName);
                                                    if (crate == null) {

                                                        player.sendMessage(FancyText.getFormattedText("&eInvalid crate name!"), player.getUniqueID());
                                                        return 0;

                                                    }

                                                    try {

                                                        Map<String, Map<String, String>> pool = BetterCrates.crateConfigManager.getConfigNode(crate.getIndex(), "Rewards", "Pool").getValue(new TypeToken<Map<String, Map<String, String>>>() {});
                                                        List<ItemStack> possiblePrizes = new ArrayList<>();
                                                        for (Map.Entry<String, Map<String, String>> entry : pool.entrySet()) {

                                                            String id = entry.getKey();
                                                            //ItemStack stack = ItemStackBuilder.buildFromStringID("minecraft:diamond");
                                                            ItemStack stack;
                                                            if (id.contains("pixelmon:pixelmon_sprite")) {

                                                                boolean shiny = false;
                                                                String[] split = id.split("/");
                                                                String[] specs = split[1].split(" ");
                                                                String speciesName = "Bulbasaur";
                                                                for (String s : specs) {

                                                                    if (s.contains("shiny:")) {

                                                                        shiny = Boolean.parseBoolean(s.replace("shiny:", ""));

                                                                    }

                                                                    if (s.contains("species:")) {

                                                                        speciesName = s.replace("species:", "");

                                                                    }

                                                                }
                                                                Pokemon pokemon = PokemonBuilder.builder().species(speciesName).shiny(shiny).build();
                                                                Species species = pokemon.getSpecies();
                                                                stack = new ItemStack(PixelmonItems.pixelmon_sprite);
                                                                CompoundNBT nbt = new CompoundNBT();
                                                                stack.setTag(nbt);
                                                                nbt.putShort("ndex", (short) species.getDex());
                                                                nbt.putString("form", species.getDefaultForm().getName());

                                                            } else {

                                                                stack = ItemStackBuilder.buildFromStringID(id);

                                                            }

                                                            if (entry.getValue().containsKey("Display-Name")) {

                                                                stack.setDisplayName(FancyText.getFormattedText(entry.getValue().get("Display-Name")));

                                                            }

                                                            ListNBT lore = new ListNBT();
                                                            lore.add(StringNBT.valueOf(FancyText.getFormattedString("&eChance: " + BetterCrates.crateConfigManager.getConfigNode(crate.getIndex(), "Rewards", "Pool", id, "Chance").getDouble())));
                                                            if (!BetterCrates.crateConfigManager.getConfigNode(crate.getIndex(), "Rewards", "Pool", id, "Lore").isVirtual()) {

                                                                List<String> loreString = BetterCrates.crateConfigManager.getConfigNode(crate.getIndex(), "Rewards", "Pool", id, "Lore").getList(TypeToken.of(String.class));

                                                                for (String s : loreString) {

                                                                    lore.add(StringNBT.valueOf(FancyText.getFormattedString(s)));

                                                                }

                                                            }

                                                            stack.getChildTag("display").put("Lore", lore);
                                                            possiblePrizes.add(stack);

                                                        }

                                                        PreviewGUI gui = new PreviewGUI(player, crate, possiblePrizes);
                                                        gui.open();

                                                    } catch (ObjectMappingException e) {

                                                        e.printStackTrace();

                                                    }

                                                }

                                                return 1;

                                            })
                                    )
                            )
            );

        }

    }

}
