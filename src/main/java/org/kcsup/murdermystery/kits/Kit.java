package org.kcsup.murdermystery.kits;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.kcsup.murdermystery.Main;

import java.util.UUID;

public class Kit {

    protected UUID uuid;
    protected KitType type;

    public Kit(UUID uuid, KitType type) {
        this.uuid = uuid;
        this.type = type;
    }

    public UUID getUUID() { return uuid; }
    public KitType getType() { return type; }


    public void remove() {
        // HandlerList.unregisterAll(this);
    }

}
