package org.kcsup.murdermystery.kits.types;

import org.bukkit.entity.Player;
import org.kcsup.murdermystery.kits.Kit;
import org.kcsup.murdermystery.kits.KitType;

import java.util.UUID;

public class Murderer extends Kit {

    public Murderer(UUID uuid) {
        super(uuid, KitType.MURDERER);
    }

    @Override
    public void onStart(Player player) {

    }
}
