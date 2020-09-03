package org.kcsup.murdermystery;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.kcsup.murdermystery.kits.KitType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Arena {

    private int id;
    private ArrayList<UUID> players;
    private HashMap<UUID, KitType> kits;
    private Location spawn;
    private GameState state;
    private Countdown countdown;
    private MurdererCountdown murdererCountdown;
    private Game game;

    public Arena(int id) {
        this.id = id;
        players = new ArrayList<>();
        kits = new HashMap<UUID, KitType>();
        spawn = Config.getArenaSpawn(id);
        state = GameState.RECRUITING;
        countdown = new Countdown(this);
        murdererCountdown = new MurdererCountdown(this);
        game = new Game(this);
    }

    public void start() {
        game.start();
        murdererCountdown.begin();
    }

    public void reset() {
        for (UUID uuid : players) {
            removeKit(uuid);
            game.removeBowStand();
            Bukkit.getPlayer(uuid).teleport(Config.getLobbySpawn());
            Bukkit.getPlayer(uuid).getInventory().clear();
            restoreInventory(Bukkit.getPlayer(uuid));
            restoreGameMode(Bukkit.getPlayer(uuid));
            game.kills.keySet().clear();
        }

        state = GameState.RECRUITING;
        players.clear();
        countdown = new Countdown(this);
        murdererCountdown = new MurdererCountdown(this);
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

    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        for (UUID uuid : players) {
            Bukkit.getPlayer(uuid).sendTitle(title,subtitle,fadeIn,stay,fadeOut);
        }
    }

    public void sendSound(Sound sound, SoundCategory category, float volume, float pitch) {
        for (UUID uuid : players) {
            Bukkit.getPlayer(uuid).playSound(Bukkit.getPlayer(uuid).getLocation(),sound, category, volume, pitch);
        }
    }

    public void addPlayer(Player player) {
        players.add(player.getUniqueId());
        player.teleport(spawn);
        sendMessage(ChatColor.GREEN + player.getName() + " has joined!");
        saveInventory(player);
        saveGameMode(player);
        player.getInventory().clear();
        player.setGameMode(GameMode.SURVIVAL);

        if (players.size() >= Config.getRequiredPlayers()) {
            countdown.begin();
        }
    }

    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
        player.teleport(Config.getLobbySpawn());

        if (getKits().get(player.getUniqueId()).equals(KitType.MURDERER) &&
                state.equals(GameState.LIVE)) {
            sendMessage(ChatColor.GOLD + "The innocents win! The murderer quit!");
            murdererCountdown.cancel();
            reset();
        }
        removeKit(player.getUniqueId());

        player.getInventory().clear();
        restoreInventory(player);
        restoreGameMode(player);

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

    public HashMap<UUID, KitType> getKits() {
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
            kits.remove(uuid);
        }
    }

    public void setKit(UUID uuid, KitType type) {
        removeKit(uuid);

        switch (type) {
            case MURDERER:
                kits.put(uuid, KitType.MURDERER);
                break;
            case DETECTIVE:
                kits.put(uuid, KitType.DETECTIVE);
                break;
            case INNOCENT:
                kits.put(uuid, KitType.INNOCENT);
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

    //---

    private HashMap<String, GameMode> mySavedGameMode = new HashMap<>();

    public void saveGameMode(Player player)
    {
        this.mySavedGameMode.put(player.getName(), copyGameMode(player.getGameMode()));
    }

    /**
     * This removes the saved inventory from our HashMap, and restores it to the player if it existed.
     * @return true iff success
     */
    public boolean restoreGameMode(Player player)
    {
        GameMode savedGameMode = this.mySavedGameMode.remove(player.getName());
        if(savedGameMode == null)
            return false;
        restoreGameMode(player,savedGameMode);
        return true;
    }

    private GameMode copyGameMode(GameMode gm)
    {
        GameMode original = gm;
        GameMode copy = null;
        if(original != null)
            copy = original;
        return copy;
    }

    private void restoreGameMode(Player p, GameMode gamemode)
    {
        p.setGameMode(gamemode);
    }
    //---

}
