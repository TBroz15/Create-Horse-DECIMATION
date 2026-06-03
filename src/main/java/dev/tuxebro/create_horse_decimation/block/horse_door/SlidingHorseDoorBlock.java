package dev.tuxebro.create_horse_decimation.block.horse_door;

import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;
import dev.tuxebro.create_horse_decimation.ModBlockEntityTypes;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SlidingHorseDoorBlock extends SlidingDoorBlock {
    public SlidingHorseDoorBlock(Properties properties) {
        super(properties, STONE_SET_TYPE.get(), true);
    }

    @Override
    public BlockEntityType<? extends SlidingHorseDoorBlockEntity> getBlockEntityType() {
        return ModBlockEntityTypes.SLIDING_HORSE_DOOR.get();
    }

}
