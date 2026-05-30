package dev.tuxebro.create_horse_decimation;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.tuxebro.create_horse_decimation.block.horse_abductor.HorseAbductorBlockEntity;

public class ModBlockEntityTypes {
    private static final CreateRegistrate REGISTRATE = CreateHorseDecimation.registrate();

    public static final BlockEntityEntry<HorseAbductorBlockEntity> HORSE_ABDUCTOR_BLOCK = REGISTRATE
            .blockEntity("horse_abductor_block", HorseAbductorBlockEntity::new)
            .validBlocks(ModBlocks.HORSE_ABDUCTOR_BLOCK)
            .register();

    public static void register() {}
}
