package org.kcsup.murdermystery;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.kcsup.murdermystery.kits.Kit;
import org.kcsup.murdermystery.kits.KitType;

import java.util.*;

public class Game {

    private Arena arena;
    private HashMap<UUID, Integer> points;
    private UUID uuidDetective;
    private UUID uuidMurderer;

    public Game(Arena arena) {
        this.arena = arena;
        this.points = new HashMap<>();
    }

    public void start() {
        for (UUID uuid : arena.getPlayers()) {
            Manager.getArena(arena.getID()).setKit(uuid, KitType.INNOCENT);
        }

        Random random = new Random();
        // assign a player murderer randomly
        int murderIndex = random.nextInt(arena.getPlayers().size());
        uuidMurderer = arena.getPlayers().get(murderIndex);

        // assign a player detective randomly
        int detectiveIndex;
        do {
            detectiveIndex = random.nextInt(arena.getPlayers().size());
        } while (detectiveIndex == murderIndex);
        uuidDetective = arena.getPlayers().get(detectiveIndex);

        Manager.getArena(arena.getID()).setKit(uuidMurderer,KitType.MURDERER);
        Manager.getArena(arena.getID()).setKit(uuidDetective,KitType.DETECTIVE);

        arena.setState(GameState.LIVE);

        for(UUID uuid : arena.getPlayers()) {
            if(arena.getKits().get(uuid).equals(KitType.INNOCENT)){
                Bukkit.getPlayer(uuid).sendTitle(KitType.INNOCENT.getTitle(), KitType.INNOCENT.getSubTitle(),10,200,20 );
            }
            if(arena.getKits().get(uuid).equals(KitType.MURDERER)) {
                Bukkit.getPlayer(uuid).sendTitle(KitType.MURDERER.getTitle(),KitType.MURDERER.getSubTitle(),10,200,20);
            }
            if(arena.getKits().get(uuid).equals(KitType.DETECTIVE)) {
                Bukkit.getPlayer(uuid).sendTitle(KitType.DETECTIVE.getTitle(),KitType.DETECTIVE.getSubTitle(),10,200,20);
            }
        }

        for(UUID uuid : arena.getPlayers()) {
            Kit kitType = arena.getKits().get(uuid);
        }

        arena.sendMessage(ChatColor.YELLOW + "-------------------------------------------------");
        arena.sendMessage(ChatColor.GREEN + "                 MURDER MYSTERY");
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
