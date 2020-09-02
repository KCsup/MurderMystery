package org.kcsup.murdermystery;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.kcsup.murdermystery.kits.Kit;
import org.kcsup.murdermystery.kits.KitType;
import org.kcsup.murdermystery.kits.types.Detective;
import org.kcsup.murdermystery.kits.types.Innocent;
import org.kcsup.murdermystery.kits.types.Murderer;
import org.kcsup.murdermystery.kits.types.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Arena {

    private int id;
    private ArrayList<UUID> players;
    private HashMap<UUID, Kit> kits;
    private Location spawn;
    private GameState state;
    private Countdown countdown;
    private Game game;

    public Arena(int id) {
        this.id = id;
        players = new ArrayList<>();
        kits = new HashMap<>();
        spawn = Config.getArenaSpawn(id);
        state = GameState.RECRUITING;
        countdown = new Countdown(this);
        game = new Game(this);
    }

    public void start() {
        game.start();
    }

    public void reset() {
        for (UUID uuid : players) {
            removeKit(uuid);
            Bukkit.getPlayer(uuid).teleport(Config.getLobbySpawn());
            Bukkit.getPlayer(uuid).getInventory().clear();
            restoreInventory(Bukkit.getPlayer(uuid));
        }

        state = GameState.RECRUITING;
        players.clear();
        countdown = new Countdown(this);
        game = new Game(this);
    }

    public void resetCountdown() {
        for (UUID uuid : players) {
            Bukkit.getPlayer(uuid).teleport(Config.getArenaSpawn(getID()));
        }
        state = GameState.RECRUITING;
        countdown = new Countdown(this);
        game = new Game(this);
    }

    public void sendMessage(String message) {
        for (UUID uuid : players) {
            Bukkit.getPlayer(uuid).sendMessage(message);
        }
    }

    public void addPlayer(Player player) {
        players.add(player.getUniqueId());
        player.teleport(spawn);
        sendMessage(ChatColor.GREEN + player.getName() + " has joined!");
        saveInventory(player);
        player.getInventory().clear();

        if (players.size() >= Config.getRequiredPlayers()) {
            countdown.begin();
        }
    }

    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
        player.teleport(Config.getLobbySpawn());

        removeKit(player.getUniqueId());

        player.getInventory().clear();
        restoreInventory(player);

        sendMessage(ChatColor.GREEN + player.getName() + " has quit!");

        if (players.size() <= Config.getRequiredPlayers() && state.equals(GameState.COUNTDOWN)) {
            resetCountdown();
        }

        if (players.size() == 0 && state.equals(GameState.LIVE) || players.size() == 1 && state.equals(GameState.LIVE)) {
            reset();
        }
    }

    public int getID() {
        return id;
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public HashMap<UUID, Kit> getKits() {
        return kits;
    }

    public GameState getState() {
        return state;
    }

    public Game getGame() {
        return game;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public void removeKit(UUID uuid) {
        if (kits.containsKey(uuid)) {
            kits.get(uuid).remove();
            kits.remove(uuid);
        }
    }

    public void setKit(UUID uuid, KitType type) {
        removeKit(uuid);

        switch (type) {
            case TEST:
                kits.put(uuid, new Test(uuid));
                break;
            case MURDERER:
                kits.put(uuid, new Murderer(uuid));
                break;
            case DETECTIVE:
                kits.put(uuid, new Detective(uuid));
                break;
            case INNOCENT:
                kits.put(uuid, new Innocent(uuid));
                break;
            default:
                return;
        }
    }

    //---
    private HashMap<String, ItemStack[]> mySavedItems = new HashMap<>();

    public void saveInventory(Player player)
    {
        this.mySavedItems.put(player.getName(), copyInventory(player.getInventory()));
    }

    /**
     * This removes the saved inventory from our HashMap, and restores it to the player if it existed.
     * @return true iff success
     */
    public boolean restoreInventory(Player player)
    {
        ItemStack[] savedInventory = this.mySavedItems.remove(player.getName());
        if(savedInventory == null)
            return false;
        restoreInventory(player, savedInventory);
        return true;
    }

    private ItemStack[] copyInventory(Inventory inv)
    {
        ItemStack[] original = inv.getContents();
        ItemStack[] copy = new ItemStack[original.length];
        for(int i = 0; i < original.length; ++i)
            if(original != null)
                copy = original;
        return copy;
    }

    private void restoreInventory(Player p, ItemStack[] inventory)
    {
        p.getInventory().setContents(inventory);
    }

}
