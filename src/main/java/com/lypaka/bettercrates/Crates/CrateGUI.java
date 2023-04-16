package com.lypaka.bettercrates.Crates;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.ItemStackBuilder;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CrateGUI {

    private final ServerPlayerEntity player;
    private final Map<ItemStack, List<String>> prizes;
    public static Map<UUID, Map<ItemStack, List<String>>> playersAbleToClaimShitInTheEventTheyAccidentallyCloseTheFuckingMenuLikeAnIdiot = new HashMap<>();

    public CrateGUI (ServerPlayerEntity player, Map<ItemStack, List<String>> prizes) {

        this.player = player;
        this.prizes = prizes;

    }

    public void open() {

        playersAbleToClaimShitInTheEventTheyAccidentallyCloseTheFuckingMenuLikeAnIdiot.put(this.player.getUniqueID(), this.prizes);
        if (this.prizes.size() > 28) {

            this.player.sendMessage(FancyText.getFormattedText("&eYou've won more stuff from the crate than the crate GUI can show, so the GUI has been bypassed."), this.player.getUniqueID());
            this.player.sendMessage(FancyText.getFormattedText("&eTo claim your prizes, use command &a\"/bettercrates claim\"&e."), this.player.getUniqueID());
            return;

        }
        ChestTemplate template = ChestTemplate.builder(6).build();
        GooeyPage page = GooeyPage.builder()
                .template(template)
                .title(FancyText.getFormattedString("&dBetterCrates Menu"))
                .build();

        int[] glass = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 50, 51, 52, 53};
        for (int i : glass) {

            ItemStack icon = ItemStackBuilder.buildFromStringID("minecraft:yellow_stained_glass_pane");
            icon.setDisplayName(FancyText.getFormattedText(""));
            page.getTemplate().getSlot(i).setButton(GooeyButton.builder().display(icon).build());

        }
        int orb = 49;
        int[] slots = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
        int index = 0;
        for (Map.Entry<ItemStack, List<String>> icons : this.prizes.entrySet()) {

            page.getTemplate().getSlot(slots[index]).setButton(GooeyButton.builder().display(icons.getKey()).build());
            index++;

        }

        page.getTemplate().getSlot(orb).setButton(getFunctionButton(this.player, this.prizes));
        UIManager.openUIForcefully(this.player, page);

    }

    private Button getFunctionButton (ServerPlayerEntity player, Map<ItemStack, List<String>> prizes) {

        ItemStack orb = ItemStackBuilder.buildFromStringID("pixelmon:jade_orb");
        orb.setDisplayName(FancyText.getFormattedText("&aClick me to claim your prizes!"));
        return GooeyButton.builder()
                .display(orb)
                .onClick(() -> {

                    UIManager.closeUI(player);
                    playersAbleToClaimShitInTheEventTheyAccidentallyCloseTheFuckingMenuLikeAnIdiot.entrySet().removeIf(entry -> entry.getKey().toString().equalsIgnoreCase(player.getUniqueID().toString()));
                    for (Map.Entry<ItemStack, List<String>> entry : prizes.entrySet()) {

                        for (String c : entry.getValue()) {

                            player.world.getServer().getCommandManager().handleCommand(player.world.getServer().getCommandSource(), c.replace("%player%", player.getName().getString()));

                        }

                    }

                })
                .build();

    }

}
