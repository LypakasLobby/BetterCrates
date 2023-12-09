package com.lypaka.bettercrates.Crates;

import com.google.common.reflect.TypeToken;
import com.lypaka.bettercrates.BetterCrates;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.List;

public class Crate {

    private final int index;
    private String crateName;
    private CrateKey crateKey;
    private List<String> crateBlockTriggers;
    private List<String> crateEntityTriggers;
    private int crateCooldown;
    private int crateCost;
    private String cratePermission;
    private CrateReward crateReward;

    public Crate (int index) {

        this.index = index;

    }

    public void load() throws ObjectMappingException {

        this.crateKey = new CrateKey(this);
        this.crateKey.create();
        this.crateBlockTriggers = BetterCrates.crateConfigManager.getConfigNode(this.index, "Crate", "Location", "Block").getList(TypeToken.of(String.class));
        this.crateEntityTriggers = BetterCrates.crateConfigManager.getConfigNode(this.index, "Crate", "Location", "Entity").getList(TypeToken.of(String.class));
        this.crateCooldown = BetterCrates.crateConfigManager.getConfigNode(this.index, "Crate", "Settings", "Cooldown").getInt();
        this.crateCost = BetterCrates.crateConfigManager.getConfigNode(this.index, "Crate", "Settings", "Cost").getInt();
        this.crateName = BetterCrates.crateConfigManager.getConfigNode(this.index, "Crate", "Settings", "Name").getString();
        this.cratePermission = BetterCrates.crateConfigManager.getConfigNode(this.index, "Crate", "Settings", "Permission").getString();
        this.crateReward = new CrateReward(this);
        BetterCrates.crates.add(this);
        BetterCrates.crateMap.put(this.crateName, this);

    }

    public int getIndex() {

        return this.index;

    }

    public CrateKey getCrateKey() {

        return this.crateKey;

    }

    public List<String> getCrateBlockTriggers() {

        return this.crateBlockTriggers;

    }

    public List<String> getCrateEntityTriggers() {

        return this.crateEntityTriggers;

    }

    public int getCrateCooldown() {

        return this.crateCooldown;

    }

    public int getCrateCost() {

        return this.crateCost;

    }

    public String getCrateName() {

        return this.crateName;

    }

    public String getCratePermission() {

        return this.cratePermission;

    }

    public CrateReward getCrateReward() {

        return this.crateReward;

    }

    public static Crate getCrateFromName (String name) {

        Crate crate = null;
        for (Crate c : BetterCrates.crates) {

            if (name.equalsIgnoreCase(c.getCrateName())) {

                crate = c;
                break;

            }

        }

        return crate;

    }

    public static Crate getCrateFromLocation (String mode, String location) {

        Crate crate = null;
        for (Crate c : BetterCrates.crates) {

            if (mode.equalsIgnoreCase("entity")) {

                if (c.getCrateEntityTriggers().contains(location)) {

                    crate = c;
                    break;

                }

            } else {

                if (c.getCrateBlockTriggers().contains(location)) {

                    crate = c;
                    break;

                }

            }

        }

        return crate;

    }

}
