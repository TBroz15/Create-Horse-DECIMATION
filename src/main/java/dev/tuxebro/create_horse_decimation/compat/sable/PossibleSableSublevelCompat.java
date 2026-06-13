package dev.tuxebro.create_horse_decimation.compat.sable;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

public class PossibleSableSublevelCompat {
    protected BlockEntity blockEntity;

    public PossibleSableSublevelCompat(BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    public Vec3 getGlobalLevelPos(Vec3 pos) {
        return pos;
    }

    public boolean isInsideOfSublevel() {
        return false;
    }
}
