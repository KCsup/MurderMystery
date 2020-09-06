package org.kcsup.murdermystery;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class Config {

    private static Main main;

    public Config(Main main) {
        Config.main = main;

        main.getConfig().options().copyDefaults();
        main.saveDefaultConfig();
    }

    public static int getRequiredPlayers() { return main.getConfig().getInt("required-players"); }

    public static int getCountdownSeconds() { return main.getConfig().getInt("countdown-seconds"); }

    public static Location getLobbySpawn() {
        return new Location(
                Bukkit.getWorld(main.getConfig().getString("lobby-spawn.world")),
                main.getConfig().getDouble("lobby-spawn.x"),
                main.getConfig().getDouble("lobby-spawn.y"),
                main.getConfig().getDouble("lobby-spawn.z"),
                main.getConfig().getInt("lobby-spawn.yaw"),
                main.getConfig().getInt("lobby-spawn.pitch"));
    }

    public static Location getArenaSpawn(int id) {
        World world = Bukkit.createWorld(new WorldCreator(main.getConfig().getString("arenas." + id + ".spawn.world")));
        world.setAutoSave(false);

        return new Location(
                world,
                main.getConfig().getDouble("arenas." + id + ".spawn.x"),
                main.getConfig().getDouble("arenas." + id + ".spawn.y"),
                main.getConfig().getDouble("arenas." + id + ".spawn.z"),
                main.getConfig().getInt("arenas." + id + ".spawn.yaw"),
                main.getConfig().getInt("arenas." + id + ".spawn.pitch"));
    }

    public static int getArenaAmount() { return main.getConfig().getConfigurationSection("arenas.").getKeys(false).size(); }

    public static Location getArenaGameSpawn(int id) {

        return new Location(
                Bukkit.getWorld(main.getConfig().getString("arenas." + id + ".game-spawn.world")),
                main.getConfig().getDouble("arenas." + id + ".game-spawn.x"),
                main.getConfig().getDouble("arenas." + id + ".game-spawn.y"),
                main.getConfig().getDouble("arenas." + id + ".game-spawn.z"),
                main.getConfig().getInt("arenas." + id + ".game-spawn.yaw"),
                main.getConfig().getInt("arenas." + id + ".game-spawn.pitch"));
    }

    public static Location getArenaSign(int id) {

        return new Location(
                Bukkit.getWorld(main.getConfig().getString("arenas." + id + ".sign.world")),
                main.getConfig().getDouble("arenas." + id + ".sign.x"),
                main.getConfig().getDouble("arenas." + id + ".sign.y"),
                main.getConfig().getDouble("arenas." + id + ".sign.z"));
    }

    public static int isSign(Location location) {
        for (int i = 0; i < getArenaAmount(); i++) {
            if(getArenaSign(i).equals(location)) {
                return i;
            }
        }

        return -1;
    }

}
