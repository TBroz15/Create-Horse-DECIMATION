package dev.tuxebro.create_horse_decimation.block.horse_block;

import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;

public class HorseBlock extends DirectionalKineticBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public HorseBlock(Properties properties) {
        super(properties.mapColor(MapColor.TERRACOTTA_BROWN)
                .instabreak()
                .sound(
                        new SoundType(1,1.25f,
                                SoundEvents.HORSE_DEATH,
                                SoundEvents.HORSE_STEP,
                                SoundEvents.HORSE_ANGRY,
                                SoundEvents.HORSE_HURT,
                                SoundEvents.HORSE_LAND)
                ));
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING)
                .getAxis();
    }
}
