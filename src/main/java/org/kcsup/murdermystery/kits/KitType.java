package org.kcsup.murdermystery.kits;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.kcsup.murdermystery.Manager;

public enum KitType {


    MURDERER(ChatColor.RED + "Murderer",
            ChatColor.translateAlternateColorCodes('&', "&c&lMURDERER"),
            ChatColor.GRAY + "Kill them all!",
            new ItemStack(Material.GOLD_INGOT,1)),
    DETECTIVE(ChatColor.BLUE + "Detective",
            ChatColor.translateAlternateColorCodes('&',"&9&lDETECTIVE"),
            ChatColor.GRAY + "Split up and search for clues!",
            new ItemStack(Material.GOLD_INGOT,1)),
    INNOCENT(ChatColor.GRAY + "Innocent",
            ChatColor.translateAlternateColorCodes('&',"&8&lINNOCENT"),
            ChatColor.GRAY + "Don't die!",
            new ItemStack(Material.GOLD_INGOT,1));

    private String display;
    public String title;
    public String subTitle;
    public ItemStack itemStacks;

    private KitType(String display,String title, String subTitle,ItemStack itemStacks) {
        this.display = display;
        this.title = title;
        this.subTitle = subTitle;
        this.itemStacks = itemStacks;
    }

    public String getDisplay() { return display; }
    public String getTitle() { return title; }
    public String getSubTitle() { return subTitle; }

    public void onStart(Player player) {
        player.getInventory().setItem(8,itemStacks);
        player.sendTitle(title, subTitle,10,200,20 );
        if(Manager.getArena(player).getKits().get(player.getUniqueId()).equals(KitType.DETECTIVE)) {
            ItemStack enchBow = new ItemStack(Material.BOW);
            enchBow.addEnchantment(Enchantment.ARROW_INFINITE,1);

            player.getInventory().setItem(1,enchBow);
            player.getInventory().setItem(9,new ItemStack(Material.ARROW));
        }

    }

}
