package dev.tuxebro.create_horse_decimation.block.horse_abductor;

import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.fan.EncasedFanBlock;
import com.simibubi.create.content.kinetics.fan.EncasedFanBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import dev.tuxebro.create_horse_decimation.ModBlockEntityTypes;
import dev.tuxebro.create_horse_decimation.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.Nullable;

public class HorseAbductorBlock extends DirectionalAxisKineticBlock implements IBE<HorseAbductorBlockEntity> {
    public HorseAbductorBlock(Properties properties) {
        super(properties);
    }

    private void updateFanBlocks(BlockState state, Level level, BlockPos pos) {
        if (level == null) return;
        if (level.isClientSide) return;

        var diherection = state.getValue(EncasedFanBlock.FACING); // sorry
        var axis = ((IRotate) state.getBlock()).getRotationAxis(state);

        var topVentDir = diherection.getOpposite();
        var sideVentDir1 = Direction.fromAxisAndDirection(axis, Direction.AxisDirection.NEGATIVE);
        var sideVentDir2 = Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE);

        var topFan = getFanFromVents(level, pos, topVentDir);
        var sideFan1 = getFanFromVents(level, pos, sideVentDir1);
        var sideFan2 = getFanFromVents(level, pos, sideVentDir2);

        var range = getRangeFromFan(topFan, sideFan1, sideFan2);

        if (!(level.getBlockEntity(pos) instanceof HorseAbductorBlockEntity fanBlockEntity)) return;

        fanBlockEntity.updateRange(range);
        level.sendBlockUpdated(pos, state, state, 3); // client update
    }

    private int getRangeFromFan(@Nullable EncasedFanBlockEntity ...fans) {
        final int maxRPM = 256;
        int rangeSum = 0;

        for (EncasedFanBlockEntity fan : fans) {
            if (fan == null) continue;

            var rpm = Math.abs(fan.getSpeed());
            if (rpm >= maxRPM) rpm = maxRPM;
            rangeSum += (int) Math.ceil((rpm / maxRPM) * 15);
        }

        return rangeSum;
    }

    // is this a monogamous reference?
    @Nullable
    private EncasedFanBlockEntity getFanFromVents(Level level, BlockPos pos, Direction direction) {
        var ventPos = pos.relative(direction);
        var ventBE = level.getBlockEntity(ventPos);

        if (ventBE == null) return null;
        if (!(ventBE instanceof EncasedFanBlockEntity topVentFan)) return null;

        var ventOppositeDir = topVentFan.getBlockState().getValue(EncasedFanBlock.FACING).getOpposite();
        if (ventOppositeDir != direction) return null; // if fan not is facing at vent

        return topVentFan;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, isMoving);

        if (level.isClientSide) return;

        updateFanBlocks(state, level, pos);
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader levelReader, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, levelReader, pos, neighbor);

        if (!(levelReader instanceof Level level)) return;
        if (level.isClientSide) return;

        updateFanBlocks(state, level, pos);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);

        if (level.isClientSide) return;

        updateFanBlocks(state, level, pos);
    }

    @Override
    public Class<HorseAbductorBlockEntity> getBlockEntityClass() {
        return HorseAbductorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends HorseAbductorBlockEntity> getBlockEntityType() {
        return ModBlockEntityTypes.HORSE_ABDUCTOR_BLOCK.get();
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() == newState.getBlock()) return;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof HorseAbductorBlockEntity abductorBE)) return;

        if (isMoving && Config.server.horseDupe.get()) {
            abductorBE.inventoryProvider().dropInv(level, pos);
            super.onRemove(state, level, pos, newState, isMoving);
            return;
        }

        if (isMoving) return;

        abductorBE.inventoryProvider().dropInv(level, pos);
        super.onRemove(state, level, pos, newState, isMoving);

    }
}
