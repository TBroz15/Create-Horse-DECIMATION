package dev.tuxebro.create_horse_decimation.compat;

import dev.tuxebro.create_horse_decimation.compat.sable.PossibleSableSublevelCompat;
import dev.tuxebro.create_horse_decimation.compat.sable.SableSublevelCompat;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.fml.ModList;

public class ModCompat {
    private static final String SABLE_MOD_ID = "sable";
    private static final boolean IS_SABLE_LOADED = ModList.get().isLoaded(SABLE_MOD_ID);

    public static PossibleSableSublevelCompat createSableSublevelCompat(BlockEntity blockEntity) {
        if (IS_SABLE_LOADED) return new SableSublevelCompat(blockEntity);
        else return new PossibleSableSublevelCompat(blockEntity);
    }
}
