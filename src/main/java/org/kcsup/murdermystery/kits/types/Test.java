package org.kcsup.murdermystery.kits.types;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.kcsup.murdermystery.GameState;
import org.kcsup.murdermystery.Manager;
import org.kcsup.murdermystery.kits.Kit;
import org.kcsup.murdermystery.kits.KitType;

import java.util.UUID;

public class Test extends Kit {

    public Test(UUID uuid) {
        super(uuid, KitType.TEST);
    }

    @Override
    public void onStart(Player player) {
        player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
    }

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (Manager.isPlaying(player) && Manager.getArena(player).getState().equals(GameState.LIVE)) {
            if (e.getCurrentItem().getType().equals(Material.IRON_SWORD)) {
                e.setCancelled(true);
            }
            if(e.getAction().equals(InventoryAction.HOTBAR_SWAP)) {
                e.setCancelled(true);
            }
        }
    }
}
