package com.lypaka.bettercrates.Crates;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.lypaka.lypakautils.FancyText;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.List;

public class PreviewGUI {

    private final ServerPlayerEntity player;
    private final Crate crate;
    private final List<ItemStack> items;

    public PreviewGUI (ServerPlayerEntity player, Crate crate, List<ItemStack> items) {

        this.player = player;
        this.crate = crate;
        this.items = items;

    }

    public void open() {

        ChestTemplate template = ChestTemplate.builder(6).build();
        GooeyPage page = GooeyPage.builder()
                .template(template)
                .title(FancyText.getFormattedString("&d" + crate.getCrateName() + " Preview"))
                .build();

        for (int i = 0; i < this.items.size(); i++) {

            page.getTemplate().getSlot(i).setButton(GooeyButton.builder().display(this.items.get(i)).build());

        }

        UIManager.openUIForcefully(this.player, page);

    }

}
