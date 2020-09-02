package org.kcsup.murdermystery.kits;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.kcsup.murdermystery.Main;

import java.util.UUID;

public abstract class Kit implements Listener {

    protected UUID uuid;
    protected KitType type;

    public Kit(UUID uuid, KitType type) {
        this.uuid = uuid;
        this.type = type;

        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    public UUID getUUID() { return uuid; }
    public KitType getType() { return type; }

    public abstract void onStart(Player player);

    public void remove() {
        HandlerList.unregisterAll(this);
    }

}
