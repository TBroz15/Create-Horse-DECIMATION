package dev.tuxebro.create_horse_decimation.interfaces;

import net.minecraft.world.entity.player.Player;

// TODO: remove soon when video is done

public interface IMixinMechanicalPistonEntity {
    void updateSpeedScale(Player player);
}
