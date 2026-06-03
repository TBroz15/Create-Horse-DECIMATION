package dev.tuxebro.create_horse_decimation;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.tuxebro.create_horse_decimation.block.horse_abductor.HorseAbductorBlockEntity;
import dev.tuxebro.create_horse_decimation.block.horse_door.SlidingHorseDoorBlockEntity;
import dev.tuxebro.create_horse_decimation.block.horse_door.SlidingHorseDoorRenderer;

public class ModBlockEntityTypes {
    private static final CreateRegistrate REGISTRATE = CreateHorseDecimation.registrate();

    public static final BlockEntityEntry<HorseAbductorBlockEntity> HORSE_ABDUCTOR_BLOCK = REGISTRATE
            .blockEntity("horse_abductor_block", HorseAbductorBlockEntity::new)
            .validBlocks(ModBlocks.HORSE_ABDUCTOR_BLOCK)
            .register();

    public static final BlockEntityEntry<SlidingHorseDoorBlockEntity> SLIDING_HORSE_DOOR =
            REGISTRATE.blockEntity("sliding_horse_door", SlidingHorseDoorBlockEntity::new)
                    .renderer(() -> SlidingHorseDoorRenderer::new)
                    .validBlocks(ModBlocks.HORSE_DOOR)
                    .register();
    public static void register() {}
}
