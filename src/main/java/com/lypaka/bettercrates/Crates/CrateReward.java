package com.lypaka.bettercrates.Crates;

import com.google.common.reflect.TypeToken;
import com.lypaka.bettercrates.BetterCrates;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.ItemStackBuilder;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBuilder;
import com.pixelmonmod.pixelmon.api.util.helpers.SpriteItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrateReward {

    private final Crate crate;

    public CrateReward (Crate crate) {

        this.crate = crate;

    }

    public Map<ItemStack, List<String>> generateReward() throws ObjectMappingException {

        int max = BetterCrates.crateConfigManager.getConfigNode(this.crate.getIndex(), "Rewards", "Amount", "Max").getInt();
        int min = BetterCrates.crateConfigManager.getConfigNode(this.crate.getIndex(), "Rewards", "Amount", "Min").getInt();
        Map<String, Map<String, String>> pool = BetterCrates.crateConfigManager.getConfigNode(this.crate.getIndex(), "Rewards", "Pool").getValue(new TypeToken<Map<String, Map<String, String>>>() {});
        int prizeAmount;
        if (max == 1) {

            prizeAmount = 1;

        } else {

            prizeAmount = BetterCrates.random.nextInt(max - min) + min;

        }
        Map<ItemStack, List<String>> prizes = new HashMap<>();
        do {

            for (int i = 0; i < prizeAmount; i++) {

                for (Map.Entry<String, Map<String, String>> entry : pool.entrySet()) {

                    if (prizes.size() == prizeAmount) break;
                    String id = entry.getKey();
                    double chance = Double.parseDouble(entry.getValue().get("Chance"));
                    if (BetterCrates.random.nextDouble() >= chance) {

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
                            stack = SpriteItemHelper.getPhoto(pokemon);

                        } else {

                            stack = ItemStackBuilder.buildFromStringID(id);

                        }

                        if (entry.getValue().containsKey("Display-Name")) {

                            stack.setDisplayName(FancyText.getFormattedText(entry.getValue().get("Display-Name")));

                        }

                        if (!BetterCrates.crateConfigManager.getConfigNode(this.crate.getIndex(), "Rewards", "Pool", id, "Lore").isVirtual()) {

                            List<String> loreString = BetterCrates.crateConfigManager.getConfigNode(this.crate.getIndex(), "Rewards", "Pool", id, "Lore").getList(TypeToken.of(String.class));
                            ListNBT lore = new ListNBT();
                            for (String s : loreString) {

                                lore.add(StringNBT.valueOf(FancyText.getFormattedString(s)));

                            }

                            stack.getChildTag("display").put("Lore", lore);

                        }

                        List<String> commands = BetterCrates.crateConfigManager.getConfigNode(this.crate.getIndex(), "Rewards", "Pool", id, "Commands").getList(TypeToken.of(String.class));
                        prizes.put(stack, commands);

                    }

                }

            }

        } while (prizes.size() < min);

        return prizes;

    }

}
