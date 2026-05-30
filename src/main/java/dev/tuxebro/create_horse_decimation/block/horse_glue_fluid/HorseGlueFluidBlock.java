package dev.tuxebro.create_horse_decimation.block.horse_glue_fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.phys.Vec3;

public class HorseGlueFluidBlock extends LiquidBlock {
    public HorseGlueFluidBlock(FlowingFluid fluid, Properties properties) {
        super(fluid, properties);
    }

    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) return;

        var vec3 = new Vec3(1/4f, 1/4f, 1/4f);
        livingEntity.makeStuckInBlock(state, vec3);
    }
}

