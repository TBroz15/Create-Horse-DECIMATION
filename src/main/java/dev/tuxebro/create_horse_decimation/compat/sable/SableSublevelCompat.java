package dev.tuxebro.create_horse_decimation.compat.sable;

import dev.ryanhcode.sable.companion.SableCompanion;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

public class SableSublevelCompat extends PossibleSableSublevelCompat {
    SubLevelAccess sublevel;

    public SableSublevelCompat(BlockEntity blockEntity) {
        super(blockEntity);
        this.sublevel = SableCompanion.INSTANCE.getContaining(blockEntity);
    }

    @Override
    public Vec3 getGlobalLevelPos(Vec3 pos) {
        if (!isInsideOfSublevel()) return pos;
        return sublevel.logicalPose().transformPosition(pos);
    }

    @Override
    public boolean isInsideOfSublevel() {
        return this.sublevel != null;
    }
}
