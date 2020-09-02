package org.kcsup.murdermystery;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.kcsup.murdermystery.kits.KitType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Game {

    private Arena arena;
    private HashMap<UUID, Integer> points;

    public Game(Arena arena) {
        this.arena = arena;
        this.points = new HashMap<>();
    }

    public void start() {
        for(UUID uuid : arena.getPlayers()) {
            Manager.getArena(arena.getID()).setKit(uuid,KitType.TEST);
        }

        arena.setState(GameState.LIVE);

        arena.sendMessage(ChatColor.YELLOW + "-------------------------------------------------");
        arena.sendMessage(ChatColor.GREEN + "                 MURDER FRENZY");
        arena.sendMessage(ChatColor.YELLOW + " The objective of this game is to kill as many");
        arena.sendMessage(ChatColor.YELLOW + "            monsters as possible!");
        arena.sendMessage(ChatColor.YELLOW + "-------------------------------------------------");

        for(UUID uuid : arena.getKits().keySet()) {
            arena.getKits().get(uuid).onStart(Bukkit.getPlayer(uuid));
        }

        for(UUID uuid : arena.getPlayers()) {
            points.put(uuid,0);
        }

    }

    public void addPoint(Player player) {
        int p = points.get(player.getUniqueId()) + 1;

        points.replace(player.getUniqueId(),p);

        if(p == 20) {
            arena.sendMessage(ChatColor.GOLD + player.getName() + " WINS!!!");
            player.sendTitle(ChatColor.GOLD + "VICTORY!",ChatColor.GRAY + "You won the game!",1,200,1);

            arena.reset();
            return;
        }
    }

}
