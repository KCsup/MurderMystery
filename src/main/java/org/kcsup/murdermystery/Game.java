package org.kcsup.murdermystery;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.kcsup.murdermystery.kits.KitType;

import java.util.*;

public class Game {

    private Arena arena;
    public HashMap<UUID, Integer> kills;
    public static UUID uuidDetective;
    public static UUID uuidMurderer;
    private Location gameSpawn;

    public Game(Arena arena) {
        this.arena = arena;
        this.kills = new HashMap<>();
        gameSpawn = Config.getArenaGameSpawn(arena.getID());
    }

    public void start() {
        for (UUID uuid : arena.getPlayers()) {
            Manager.getArena(arena.getID()).setKit(uuid, KitType.INNOCENT);
            Bukkit.getPlayer(uuid).teleport(gameSpawn);
        }

        Random random = new Random();
        // assign a player murderer randomly
        int murderIndex = random.nextInt(arena.getPlayers().size());
        uuidMurderer = arena.getPlayers().get(murderIndex);

        // assign a player detective randomly
        int detectiveIndex;
        detectiveIndex = random.nextInt(arena.getPlayers().size());
        do {
            detectiveIndex = random.nextInt(arena.getPlayers().size());
        } while (detectiveIndex == murderIndex);
        uuidDetective = arena.getPlayers().get(detectiveIndex);

        Manager.getArena(arena.getID()).setKit(uuidMurderer,KitType.MURDERER);
        Manager.getArena(arena.getID()).setKit(uuidDetective,KitType.DETECTIVE);

        arena.setState(GameState.LIVE);

        arena.updateSign(ChatColor.RED + "Murder Mystery",ChatColor.WHITE + "Arena" + arena.getID(),ChatColor.DARK_RED + "[LIVE]",ChatColor.GRAY + "Players: " + arena.getPlayers().size());

        for(UUID uuid : arena.getKits().keySet()) {
            arena.getKits().get(uuid).onStart(Bukkit.getPlayer(uuid));
        }

        for(UUID uuid : arena.getPlayers()) {
            KitType kitType = arena.getKits().get(uuid);
        }

        arena.sendMessage(ChatColor.YELLOW + "-------------------------------------------------");
        arena.sendMessage(ChatColor.GREEN + "                 MURDER MYSTERY");
        arena.sendMessage(ChatColor.YELLOW + " The objective of this game is to kill as many");
        arena.sendMessage(ChatColor.YELLOW + "      players as you can as the murderer,");
        arena.sendMessage(ChatColor.YELLOW + "and to try and stop the murderer as the detective!");
        arena.sendMessage(ChatColor.YELLOW + "-------------------------------------------------");

        for(UUID uuid : arena.getPlayers()) {
            kills.put(uuidMurderer,0);
        }

    }

    public void addKill() {
        int k = kills.get(uuidMurderer) + 1;

        kills.replace(uuidMurderer,k);

        if(k >= arena.getPlayers().size() - 1) {
            arena.sendMessage(ChatColor.GOLD + "The murderer (" + Bukkit.getPlayer(uuidMurderer).getName() + ") won!");
            arena.reset();
            return;
        }
    }

     /* public void removeBowStand() {
        Player player = Bukkit.getPlayer(uuidMurderer);
        Location location = player.getLocation();
        for(Entity entity : Bukkit.getWorld(uuidMurderer).getEntities()) {
            if(entity.getType().equals(EntityType.ARMOR_STAND) && entity.getCustomName().equals("DETECTIVE'S BOW")) {
                entity.remove();
            }
        }


    } */

}
