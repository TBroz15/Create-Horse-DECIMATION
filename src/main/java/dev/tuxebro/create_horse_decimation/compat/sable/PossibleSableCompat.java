package dev.tuxebro.create_horse_decimation.compat.sable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

public class PossibleSableCompat {
    public boolean isBlockEntityInSubLevel(BlockEntity blockEntity) {
        return false;
    }

    public BlockPos getLevelPosFromSubLevelPos(BlockPos pos, BlockEntity blockEntity) {
        return pos;
    };
}
