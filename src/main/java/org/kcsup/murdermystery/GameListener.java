package org.kcsup.murdermystery;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GameListener implements Listener {

    @EventHandler
    public void onMobKill(EntityDeathEvent e) {
        Player player = e.getEntity().getKiller();

        if(Manager.isPlaying(player) && Manager.getArena(player).getState().equals(GameState.LIVE)) {
            Manager.getArena(player).getGame().addPoint(player);
            player.sendMessage(ChatColor.GREEN + "+1 Point!");
        }
    }

    @EventHandler
    public void playerHitEvent(EntityDamageByEntityEvent e) {
        try {
            Player player = (Player) e.getDamager();
            if(Manager.isPlaying(player) && Manager.getArena(player).getState().equals(GameState.COUNTDOWN) || Manager.getArena(player).getState().equals(GameState.RECRUITING)) {
                e.setCancelled(true);
            }
            if(Manager.isPlaying(player) && Manager.getArena(player).getState().equals(GameState.LIVE)) {
                if(e.getEntityType().equals(EntityType.PLAYER)) {
                    e.setCancelled(true);
                }
            }
        } catch (ClassCastException x) {

        }
    }

    @EventHandler
    public void itemDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if(Manager.isPlaying(player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickup(PlayerAttemptPickupItemEvent e){
        Player player = e.getPlayer();
        if(Manager.isPlaying(player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void breakEvent(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if(Manager.isPlaying(player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if(Manager.isPlaying(player)) {
            Manager.getArena(player).removePlayer(player);
        }
    }
}
