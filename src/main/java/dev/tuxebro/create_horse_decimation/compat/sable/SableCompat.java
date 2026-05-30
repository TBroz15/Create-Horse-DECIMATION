package dev.tuxebro.create_horse_decimation.compat.sable;

import dev.ryanhcode.sable.companion.SableCompanion;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SableCompat extends PossibleSableCompat {
    @Override
    public BlockPos getLevelPosFromSubLevelPos(BlockPos pos, BlockEntity blockEntity) {
        SubLevelAccess sublevel = SableCompanion.INSTANCE.getContaining(blockEntity);
        var isInsideBlockEntity = sublevel != null;
        if (!isInsideBlockEntity) return pos;

        pos = BlockPos.containing(
                sublevel.logicalPose().transformPosition(pos.getCenter())
        );

        return pos;
    }

    public boolean isBlockEntityInSubLevel(BlockEntity blockEntity) {
        SubLevelAccess sublevel = SableCompanion.INSTANCE.getContaining(blockEntity);
        return sublevel != null;
    }
}
