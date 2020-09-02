package org.kcsup.murdermystery.kits.types;

import org.bukkit.entity.Player;
import org.kcsup.murdermystery.kits.Kit;
import org.kcsup.murdermystery.kits.KitType;

import java.util.UUID;

public class Innocent extends Kit {

    public Innocent(UUID uuid) {
        super(uuid, KitType.INNOCENT);
    }

    @Override
    public void onStart(Player player) {

    }
}
