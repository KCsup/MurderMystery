package org.kcsup.murdermystery;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.kcsup.murdermystery.kits.KitType;

public class MurdererCountdown extends BukkitRunnable {
    private Arena arena;
    private int seconds;

    public MurdererCountdown(Arena arena) {
        this.arena = arena;
        this.seconds = 35;
    }

    public void begin() {

        this.runTaskTimer(Main.getInstance(),0,20);

    }

    @Override
    public void run() {
        if(seconds == 0) {
            cancel();
            if(arena.getKits().get(Game.uuidMurderer).equals(KitType.MURDERER)) {
                Bukkit.getPlayer(Game.uuidMurderer).getInventory().setItem(1,new ItemStack(Material.IRON_SWORD));
            }
            return;
        }

        if(seconds % 30 == 0 || seconds <= 10) {
            if(seconds == 1) {
                arena.sendMessage(ChatColor.GREEN + "The murderer will get their weapon in 1 second.");
            } else {
                arena.sendMessage(ChatColor.GREEN + "The murderer will get their weapon in " + seconds + " seconds.");
            }
        }

         /* if(arena.getState().equals(GameState.LIVE)) {
            if (!(arena.getPlayers().contains(Game.uuidMurderer))) {
                return;
            }
        } */

        seconds--;
    }
}
