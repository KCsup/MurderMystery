package org.kcsup.murdermystery;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.kcsup.murdermystery.kits.KitType;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class GameListener implements Listener {

    @EventHandler
    public void playerHitEvent(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player) {
        Player player = (Player) e.getDamager();
            if(Manager.isPlaying(player) && Manager.getArena(player).getState().equals(GameState.COUNTDOWN) || Manager.getArena(player).getState().equals(GameState.RECRUITING)) {
                e.setCancelled(true);
            }
            if(Manager.isPlaying(player) && Manager.getArena(player).getState().equals(GameState.LIVE)) {
                if (e.getEntityType().equals(EntityType.PLAYER)) {
                    Player victim = (Player) e.getEntity();
                    if (Manager.getArena(player).getKits().get(player.getUniqueId()).equals(KitType.MURDERER) &&
                            Bukkit.getPlayer(Game.uuidMurderer).getItemInHand().getType().equals(Material.IRON_SWORD)) {
                        Manager.getArena(player).getGame().addKill();
                        e.setCancelled(true);
                        Manager.getArena(player).sendSound(Sound.ENTITY_PLAYER_DEATH,
                                SoundCategory.MASTER,
                                10.0F, 1.0F);
                        victim.setGameMode(GameMode.SPECTATOR);
                        victim.getInventory().clear();
                        if (Manager.getArena(player).getKits().get(victim.getUniqueId()).equals(KitType.DETECTIVE)) {
                            Manager.getArena(player).sendTitle(ChatColor.YELLOW + "Bow dropped!", "",
                                    10, 50, 20);

                            ArmorStand bowStand = (ArmorStand) victim.getLocation().getWorld().spawnEntity(victim.getLocation(), EntityType.ARMOR_STAND);
                            bowStand.setGravity(false);
                            bowStand.setInvulnerable(true);
                            bowStand.setCustomName("DETECTIVE'S BOW");
                            bowStand.setVisible(false);
                            bowStand.setCanMove(false);
                            bowStand.setItemInHand(new ItemStack(Material.BOW));

                        }
                        if (Manager.getArena(player).getKits().get(victim.getUniqueId()).equals(KitType.INNOCENT) &&
                                victim.getInventory().getItem(1).getType().equals(Material.BOW)) {
                            Manager.getArena(player).sendTitle(ChatColor.YELLOW + "Bow dropped!", "",
                                    10, 50, 20);

                            ArmorStand bowStand = (ArmorStand) victim.getLocation().getWorld().spawnEntity(victim.getLocation(), EntityType.ARMOR_STAND);
                            bowStand.setGravity(false);
                            bowStand.setInvulnerable(true);
                            bowStand.setCustomName("DETECTIVE'S BOW");
                            bowStand.setVisible(false);
                            bowStand.setCanMove(false);
                            bowStand.setItemInHand(new ItemStack(Material.BOW));

                        }
                        victim.sendTitle(ChatColor.RED + "You died!",
                                ChatColor.GRAY + "The murderer (" + Bukkit.getPlayer(Game.uuidMurderer).getName() + ") killed you!",
                                10, 100, 20);
                    }
                    e.setCancelled(true);
                }
            }
        } else if(e.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) e.getDamager();
            Player shooter = (Player) arrow.getShooter();
            if(e.getEntity() instanceof Player) {
                Player victim = (Player) e.getEntity();
                if((Manager.isPlaying(shooter) && Manager.isPlaying(victim)) && (Manager.getArena(shooter) == Manager.getArena(victim))) {
                    if(Manager.getArena(shooter).getKits().get(shooter.getUniqueId()).equals(KitType.INNOCENT) ||
                            Manager.getArena(shooter).getKits().get(shooter.getUniqueId()).equals(KitType.DETECTIVE)) {
                        if (Manager.getArena(shooter).getKits().get(victim.getUniqueId()).equals(KitType.MURDERER)) {
                            e.setCancelled(true);
                            arrow.remove();

                            Manager.getArena(shooter).sendMessage(ChatColor.GOLD + "The innocents win! The murderer (" + Bukkit.getPlayer(Game.uuidMurderer).getName() + ") was stopped!!!");

                            Manager.getArena(shooter).reset();
                        } else if (Manager.getArena(shooter).getKits().get(victim.getUniqueId()).equals(KitType.INNOCENT)) {
                            e.setCancelled(true);
                            arrow.remove();

                            shooter.setGameMode(GameMode.SPECTATOR);
                            shooter.getInventory().clear();
                            shooter.sendTitle(ChatColor.RED + "You died!",
                                    ChatColor.GRAY + "You shot an innocent player (" + victim.getName() + ")!",
                                    10, 100, 20);

                            victim.setGameMode(GameMode.SPECTATOR);
                            shooter.getInventory().clear();
                            victim.sendTitle(ChatColor.RED + "You died!",
                                    ChatColor.GRAY + "An innocent player (" + shooter.getName() + ") thought you were the murderer!",
                                    10, 100, 20);

                            ArmorStand bowStand = (ArmorStand) shooter.getLocation().getWorld().spawnEntity(shooter.getLocation(), EntityType.ARMOR_STAND);
                            bowStand.setGravity(false);
                            bowStand.setInvulnerable(true);
                            bowStand.setCustomName("DETECTIVE'S BOW");
                            bowStand.setVisible(false);
                            bowStand.setCanMove(false);
                            bowStand.setItemInHand(new ItemStack(Material.BOW));

                            Manager.getArena(shooter).sendSound(Sound.ENTITY_PLAYER_DEATH,
                                    SoundCategory.MASTER,
                                    10.0F, 1.0F);
                            Manager.getArena(shooter).sendSound(Sound.ENTITY_PLAYER_DEATH,
                                    SoundCategory.MASTER,
                                    10.0F, 1.0F);
                            Manager.getArena(shooter).getGame().addKill();
                            Manager.getArena(shooter).getGame().addKill();
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Location location = player.getLocation();
        for(Entity entity : location.getChunk().getEntities()) {
            if(location.distanceSquared(entity.getLocation()) < (2*2)) {
                if(entity.getType().equals(EntityType.ARMOR_STAND) && entity.getCustomName().equals("DETECTIVE'S BOW")) {
                    if (!(player.getGameMode().equals(GameMode.SPECTATOR)) &&
                    Manager.getArena(player).getKits().get(player.getUniqueId()).equals(KitType.INNOCENT)) {
                        ItemStack enchBow = new ItemStack(Material.BOW);
                        enchBow.addEnchantment(Enchantment.ARROW_INFINITE, 1);

                        Manager.getArena(player).sendTitle(ChatColor.YELLOW + "Bow picked up!", "",
                                10, 50, 20);

                        entity.remove();
                        player.getInventory().setItem(1, enchBow);
                        player.getInventory().setItem(9, new ItemStack(Material.ARROW));
                    }
                }

            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        /* Player player = e.getPlayer();
        if(Manager.isPlaying(player) && Manager.getArena(player).getState().equals(GameState.LIVE)) {
            if(Manager.getArena(player).getKits().get(player.getUniqueId()).equals(KitType.INNOCENT)) {
                if (e.getAction() != Action.LEFT_CLICK_AIR && e.getAction() != Action.LEFT_CLICK_BLOCK &&
                e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) {
                    if (player.getTargetEntity(3).getType().equals(EntityType.ARMOR_STAND) &&
                            player.getTargetEntity(3).getCustomName().equals("DETECTIVE'S BOW")) {
                        ItemStack enchBow = new ItemStack(Material.BOW);
                        enchBow.addEnchantment(Enchantment.ARROW_INFINITE,1);

                        Manager.getArena(player).sendTitle(ChatColor.YELLOW + "Bow picked up!","",
                                10,200,20);

                        player.getTargetEntity(3).remove();
                        player.getInventory().setItem(1,enchBow);
                        player.getInventory().setItem(9,new ItemStack(Material.ARROW));
                    }
                }
            }
        } */
    }

    @EventHandler
    public void armorstandManipulate(PlayerArmorStandManipulateEvent e) {
        Player player = e.getPlayer();
        if(Manager.isPlaying(player) && Manager.getArena(player).getState().equals(GameState.LIVE)) {
            if(e.getRightClicked().getCustomName().equals("DETECTIVE'S BOW")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHungerLoss(FoodLevelChangeEvent e) {
        Player player = (Player) e.getEntity();
        if(Manager.isPlaying(player)) {
            e.setCancelled(true);
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
    public void inventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
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
