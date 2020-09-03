package org.kcsup.murdermystery;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;

            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("list")) {
                    player.sendMessage(ChatColor.GREEN + "The available arenas are:");
                    for(Arena arena : Manager.getArenas()) {
                        player.sendMessage(ChatColor.GREEN + "- " + arena.getID() + " [" + arena.getState() + "]");
                    }
                } else if(args[0].equalsIgnoreCase("leave")) {
                    if(Manager.isPlaying(player)) {
                        Manager.getArena(player).removePlayer(player);

                        player.sendMessage(ChatColor.GREEN + "You left the arena.");
                    } else {
                        player.sendMessage(ChatColor.RED + "You are not currently in an arena!");
                    }
                }
            } else if(args.length == 2) {
                if(args[0].equalsIgnoreCase("join")) {
                    try {
                        int id = Integer.parseInt(args[1]);

                        if(id >= 0 && id <= (Config.getArenaAmount() - 1)) {
                            if(Manager.isRecruiting(id) || Manager.isCountingDown(id)) {
                                Manager.getArena(id).addPlayer(player);

                                player.sendMessage(ChatColor.GREEN + "You are now playing in murder mystery, arena " + id + "!");
                            } else {
                                player.sendMessage(ChatColor.RED + "This game is currently live!");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "Invalid arena!");
                            player.sendMessage(ChatColor.RED + "See: /mm list for available arenas.");
                        }
                    } catch (NumberFormatException x) {
                        player.sendMessage(ChatColor.RED + "Invalid arena!");
                        player.sendMessage(ChatColor.RED + "See: /mm list for available arenas.");
                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + "Invalid usage. Correct usage is:");
                player.sendMessage(ChatColor.RED + "- /mm list");
                player.sendMessage(ChatColor.RED + "- /mm join [id]");
                player.sendMessage(ChatColor.RED + "- /mm leave");

            }
        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
        }

        return false;
    }
}
