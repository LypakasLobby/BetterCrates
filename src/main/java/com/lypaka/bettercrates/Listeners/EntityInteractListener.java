package com.lypaka.bettercrates.Listeners;

import com.lypaka.bettercrates.API.CrateOpenEvent;
import com.lypaka.bettercrates.ConfigGetters;
import com.lypaka.bettercrates.Crates.Crate;
import com.lypaka.bettercrates.Crates.CrateGUI;
import com.lypaka.bettercrates.Crates.CrateKey;
import com.lypaka.bettercrates.Crates.CrateReward;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.PermissionHandler;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityInteractListener {

    @SubscribeEvent
    public void onEntityInteract (PlayerInteractEvent.EntityInteractSpecific event) throws ObjectMappingException {

        if (event.getSide() == LogicalSide.CLIENT) return;
        if (event.getHand() == Hand.OFF_HAND) return;

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        Entity entity = event.getTarget();
        BlockPos pos = entity.getPosition();
        String worldName = player.getServerWorld().getWorld().toString().replace("ServerLevel[", "").replace("]", "");
        String location = worldName + "," + pos.getX() + "," + pos.getY() + "," + pos.getZ();
        if (player.getHeldItem(Hand.MAIN_HAND).getItem() == Items.GOLDEN_SWORD) {

            event.setCanceled(true);
            player.sendMessage(FancyText.getFormattedText("&eEntity's location: &a" + location), player.getUniqueID());

        } else {

            Crate crate = Crate.getCrateFromLocation("block", location);
            if (crate != null) {

                event.setCanceled(true);
                CrateKey key = crate.getCrateKey();
                String keyID = key.getKey().getItem().getRegistryName().toString();
                String keyName = FancyText.getFormattedString(key.getKeyDisplayName());
                if (player.getHeldItem(Hand.MAIN_HAND).getItem() != null) {

                    Item item = player.getHeldItem(Hand.MAIN_HAND).getItem();
                    String id = item.getRegistryName().toString();
                    String name = player.getHeldItem(Hand.MAIN_HAND).getDisplayName().getString();
                    if (name.equalsIgnoreCase(keyName) && keyID.equalsIgnoreCase(id)) {

                        if (crate.getCrateCooldown() > 0) {

                            Map<String, String> map = new HashMap<>();
                            if (ConfigGetters.crateCooldowns.containsKey(crate.getCrateName())) {

                                map = ConfigGetters.crateCooldowns.get(crate.getCrateName());

                            }
                            if (map.containsKey(player.getUniqueID().toString())) {

                                LocalDateTime expireTime = LocalDateTime.parse(map.get(player.getUniqueID().toString()));
                                if (!expireTime.isAfter(LocalDateTime.now())) {

                                    player.sendMessage(FancyText.getFormattedText("&eCooldown for this crate has not expired!"), player.getUniqueID());
                                    return;

                                }

                            }

                        }

                        PlayerPartyStorage storage = StorageProxy.getParty(player.getUniqueID());
                        int playerBalance = storage.getBalance().intValue();
                        if (crate.getCrateCost() > 0) {

                            if (playerBalance < crate.getCrateCost()) {

                                player.sendMessage(FancyText.getFormattedText("&eInsufficient funds to use this crate!"), player.getUniqueID());
                                return;

                            }

                        }
                        if (!crate.getCratePermission().equalsIgnoreCase("")) {

                            if (!PermissionHandler.hasPermission(player, crate.getCratePermission())) {

                                player.sendMessage(FancyText.getFormattedText("&eYou don't have permission to use this crate!"), player.getUniqueID());
                                return;

                            }

                        }
                        CrateReward reward = crate.getCrateReward();
                        Map<ItemStack, List<String>> prizes = reward.generateReward();
                        CrateOpenEvent openEvent = new CrateOpenEvent(player, crate, prizes);
                        MinecraftForge.EVENT_BUS.post(openEvent);
                        if (!openEvent.isCanceled()) {

                            player.getHeldItem(Hand.MAIN_HAND).setCount(player.getHeldItem(Hand.MAIN_HAND).getCount() - 1);
                            if (crate.getCrateCost() > 0) {

                                storage.setBalance(storage.getBalance().intValue() - crate.getCrateCost());

                            }
                            if (crate.getCrateCooldown() > 0) {

                                Map<String, String> map = new HashMap<>();
                                if (ConfigGetters.crateCooldowns.containsKey(crate.getCrateName())) {

                                    map = ConfigGetters.crateCooldowns.get(crate.getCrateName());

                                }
                                map.put(player.getUniqueID().toString(), LocalDateTime.now().plusSeconds(crate.getCrateCooldown()).toString());
                                ConfigGetters.crateCooldowns.put(crate.getCrateName(), map);

                            }

                            CrateGUI gui = new CrateGUI(player, openEvent.getPrizes());
                            gui.open();

                        }

                    }

                }

            }

        }

    }

}
