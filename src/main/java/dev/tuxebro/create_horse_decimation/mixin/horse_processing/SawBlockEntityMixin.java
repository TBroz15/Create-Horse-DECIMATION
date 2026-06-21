package dev.tuxebro.create_horse_decimation.mixin.horse_processing;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.kinetics.saw.SawBlockEntity;
import com.simibubi.create.content.processing.recipe.ProcessingInventory;
import dev.tuxebro.create_horse_decimation.ModBlocks;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SawBlockEntity.class)
public class SawBlockEntityMixin {
    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/processing/recipe/ProcessingInventory;getStackInSlot(I)Lnet/minecraft/world/item/ItemStack;", ordinal = 5))
    private ItemStack onSawProcessed(ProcessingInventory instance, int i, Operation<ItemStack> original) {
        ItemStack processedItem = original.call(instance, i);
        if (!processedItem.is(ModBlocks.HORSE_DOOR.asItem()))
            return processedItem;

        var self = (SawBlockEntity) (Object) this;

        var pos = self.getBlockPos();
        var level = self.getLevel();
        if (!(level instanceof ServerLevel serverLevel))
            return processedItem;

        serverLevel.playSound(
                null,
                pos.getX(),
                pos.getY(),
                pos.getZ(),
                SoundEvents.HORSE_HURT,
                SoundSource.BLOCKS,
                0.25f,
                1.0f);

        return processedItem;
    }
}
