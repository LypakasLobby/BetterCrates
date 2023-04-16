package com.lypaka.bettercrates.Crates;

import com.google.common.reflect.TypeToken;
import com.lypaka.bettercrates.BetterCrates;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.ItemStackBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrateKey {

    private final Crate crate;
    private ItemStack key;
    private String keyDisplayName = "";
    public static Map<Crate, CrateKey> keyMap = new HashMap<>();

    public CrateKey (Crate crate) {

        this.crate = crate;

    }

    public void create() {

        try {

            String id = BetterCrates.crateConfigManager.getConfigNode(this.crate.getIndex(), "Crate", "Key", "Display-ID").getString();
            this.key = ItemStackBuilder.buildFromStringID(id);
            if (!BetterCrates.crateConfigManager.getConfigNode(this.crate.getIndex(), "Crate", "Key", "Display-Name").isVirtual()) {

                String name = BetterCrates.crateConfigManager.getConfigNode(this.crate.getIndex(), "Crate", "Key", "Display-Name").getString();
                this.key.setDisplayName(FancyText.getFormattedText(name));
                this.keyDisplayName = name;

            }
            if (!BetterCrates.crateConfigManager.getConfigNode(this.crate.getIndex(), "Crate", "Key", "Lore").isVirtual()) {

                List<String> loreString = new ArrayList<>();
                try {

                    loreString = BetterCrates.crateConfigManager.getConfigNode(this.crate.getIndex(), "Crate", "Key", "Lore").getList(TypeToken.of(String.class));

                } catch (ObjectMappingException e) {

                    e.printStackTrace();

                }

                ListNBT lore = new ListNBT();
                for (String s : loreString) {

                    lore.add(StringNBT.valueOf(FancyText.getFormattedString(s)));

                }

                this.key.getChildTag("display").put("Lore", lore);

            }

            keyMap.put(this.crate, this);

        } catch (NullPointerException er) {



        }

    }

    public Crate getCrate() {

        return this.crate;

    }

    public ItemStack getKey() {

        try {

            String id = BetterCrates.crateConfigManager.getConfigNode(this.crate.getIndex(), "Crate", "Key", "Display-ID").getString();
            this.key = ItemStackBuilder.buildFromStringID(id);
            if (!BetterCrates.crateConfigManager.getConfigNode(this.crate.getIndex(), "Crate", "Key", "Display-Name").isVirtual()) {

                String name = BetterCrates.crateConfigManager.getConfigNode(this.crate.getIndex(), "Crate", "Key", "Display-Name").getString();
                this.key.setDisplayName(FancyText.getFormattedText(name));
                this.keyDisplayName = name;

            }
            if (!BetterCrates.crateConfigManager.getConfigNode(this.crate.getIndex(), "Crate", "Key", "Lore").isVirtual()) {

                List<String> loreString = new ArrayList<>();
                try {

                    loreString = BetterCrates.crateConfigManager.getConfigNode(this.crate.getIndex(), "Crate", "Key", "Lore").getList(TypeToken.of(String.class));

                } catch (ObjectMappingException e) {

                    e.printStackTrace();

                }

                ListNBT lore = new ListNBT();
                for (String s : loreString) {

                    lore.add(StringNBT.valueOf(FancyText.getFormattedString(s)));

                }

                this.key.getChildTag("display").put("Lore", lore);

            }

            keyMap.put(this.crate, this);

        } catch (NullPointerException er) {



        }

        return this.key;

    }

    public String getKeyDisplayName() {

        return this.keyDisplayName;

    }

}
