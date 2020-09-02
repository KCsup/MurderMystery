package org.kcsup.murdermystery.kits;

import org.bukkit.ChatColor;

public enum KitType {

    MURDERER(ChatColor.RED + "Murderer",
            ChatColor.translateAlternateColorCodes('&', "&c&lMURDERER"),
            ChatColor.GRAY + "Kill them all!"),
    DETECTIVE(ChatColor.BLUE + "Detective",
            ChatColor.translateAlternateColorCodes('&',"&9&lDETECTIVE"),
            ChatColor.GRAY + "Split up and search for clues!"),
    INNOCENT(ChatColor.GRAY + "Innocent",
            ChatColor.translateAlternateColorCodes('&',"&8&lINNOCENT"),
            ChatColor.GRAY + "Don't die!"),
    TEST(ChatColor.WHITE + "Test","test","test");

    private String display;
    public String title;
    public String subTitle;

    private KitType(String display,String title, String subTitle) {
        this.display = display;
        this.title = title;
        this.subTitle = subTitle;
    }

    public String getDisplay() { return display; }
    public String getTitle() { return title; }
    public String getSubTitle() { return subTitle; }

}
