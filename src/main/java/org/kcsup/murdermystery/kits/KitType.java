package org.kcsup.murdermystery.kits;

import org.bukkit.ChatColor;

public enum KitType {

    MURDERER(ChatColor.RED + "Murderer"),
    DETECTIVE(ChatColor.BLUE + "Detective"),
    INNOCENT(ChatColor.GRAY + "Innocent"),
    TEST(ChatColor.WHITE + "Test");

    private String display;

    private KitType(String display) {
        this.display = display;
    }

    public String getDisplay() { return display; }

}
