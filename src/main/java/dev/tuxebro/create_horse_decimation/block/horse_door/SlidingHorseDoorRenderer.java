package dev.tuxebro.create_horse_decimation.block.horse_door;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.tuxebro.create_horse_decimation.ModPartialModels;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.phys.Vec3;

public class SlidingHorseDoorRenderer extends SlidingDoorRenderer {
    public SlidingHorseDoorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(SlidingDoorBlockEntity doorBE, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        SlidingHorseDoorBlockEntity be = (SlidingHorseDoorBlockEntity) doorBE;

        BlockState blockState = be.getBlockState();
        if (!be.shouldRenderSpecial(blockState))
            return;

        Direction facing = blockState.getValue(DoorBlock.FACING);

        float value = be.animation.getValue(partialTicks);
        float value2 = Mth.clamp(value * 10, 0, 1);

        VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());

        Couple<PartialModel> partials =
                ModPartialModels.FOLDING_DOORS.get(BuiltInRegistries.BLOCK.getKey(blockState.getBlock()));

        boolean flip = blockState.getValue(DoorBlock.HINGE) == DoorHingeSide.RIGHT;
        for (boolean left : Iterate.trueAndFalse) {
            SuperByteBuffer partial = CachedBuffers.partial(partials.get(left ^ flip), blockState);
            float f = flip ? -1 : 1;

            partial.translate(0, -1 / 512f, 0)
                    .translate(Vec3.atLowerCornerOf(facing.getNormal())
                            .scale(value2 * 1 / 32f));
            partial.rotateCentered(
                    Mth.DEG_TO_RAD * AngleHelper.horizontalAngle(facing.getClockWise()), Direction.UP);

            if (flip)
                partial.translate(0, 0, 1);
            partial.rotateYDegrees(91 * f * value * value);

            if (!left)
                partial.translate(0, 0, f / 2f)
                        .rotateYDegrees(-181 * f * value * value);

            if (flip)
                partial.translate(0, 0, -1 / 2f);

            partial.light(light)
                    .renderInto(ms, vb);
        }
    }
}
