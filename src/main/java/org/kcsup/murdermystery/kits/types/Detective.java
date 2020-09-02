package org.kcsup.murdermystery.kits.types;

import org.bukkit.entity.Player;
import org.kcsup.murdermystery.kits.Kit;
import org.kcsup.murdermystery.kits.KitType;

import java.util.UUID;

public class Detective extends Kit {

    public Detective(UUID uuid) {
        super(uuid, KitType.DETECTIVE);
    }

    @Override
    public void onStart(Player player) {

    }
}
