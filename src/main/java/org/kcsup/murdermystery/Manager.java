package org.kcsup.murdermystery;

import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class Manager {

    private static ArrayList<Arena> arenas;

    public Manager() {
        arenas = new ArrayList<>();

        for(int id = 0; id <= (Config.getArenaAmount() - 1); id++) {
            arenas.add(new Arena(id));
        }
    }

    public static List<Arena> getArenas() { return arenas; }

    public static boolean isPlaying(Player player) {
        for (Arena arena : arenas) {
            if(arena.getPlayers().contains(player.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    public static Arena getArena(Player player) {
        for(Arena arena : arenas) {
            if(arena.getPlayers().contains(player.getUniqueId())) {
                return arena;
            }
        }
        return null;
    }

    public static Arena getArena(int id) {
        for(Arena arena : arenas) {
            if(arena.getID() == id) {
                return arena;
            }
        }
        return null;
    }

    public static boolean isRecruiting(int id) { return getArena(id).getState() == GameState.RECRUITING; }

    public static boolean isCountingDown(int id) { return getArena(id).getState() == GameState.COUNTDOWN; }

    public static boolean isArenaWorld(World world) {
        for(Arena arena : arenas) {
            if(arena.getSpawn().getWorld().getName().contentEquals(world.getName())) {
                return true;
            }
        }
        return false;
    }

    public static Arena getArena(World world) {
        for(Arena arena : arenas) {
            if(arena.getSpawn().getWorld().getName().contentEquals(world.getName())) {
                return arena;
            }
        }
        return null;
    }

}
