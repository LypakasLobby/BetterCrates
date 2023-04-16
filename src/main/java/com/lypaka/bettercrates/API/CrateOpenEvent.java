package com.lypaka.bettercrates.API;

import com.lypaka.bettercrates.Crates.Crate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;
import java.util.Map;

@Cancelable
public class CrateOpenEvent extends Event {

    private final ServerPlayerEntity player;
    private final Crate crate;
    private Map<ItemStack, List<String>> prizes;

    public CrateOpenEvent (ServerPlayerEntity player, Crate crate, Map<ItemStack, List<String>> prizes) {

        this.player = player;
        this.crate = crate;
        this.prizes = prizes;

    }

    public ServerPlayerEntity getPlayer() {

        return this.player;

    }

    public Crate getCrate() {

        return this.crate;

    }

    public Map<ItemStack, List<String>> getPrizes() {

        return this.prizes;

    }

    public void setPrizes (Map<ItemStack, List<String>> prizes) {

        this.prizes = prizes;

    }

}
