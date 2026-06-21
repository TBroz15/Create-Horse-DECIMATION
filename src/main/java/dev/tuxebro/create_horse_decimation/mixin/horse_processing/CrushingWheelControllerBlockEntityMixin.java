package dev.tuxebro.create_horse_decimation.mixin.horse_processing;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.kinetics.crusher.CrushingWheelControllerBlockEntity;
import com.simibubi.create.content.processing.recipe.ProcessingInventory;
import dev.tuxebro.create_horse_decimation.CreateHorseDecimation;
import dev.tuxebro.create_horse_decimation.ModItems;
import dev.tuxebro.create_horse_decimation.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CrushingWheelControllerBlockEntity.class, remap = false)
public abstract class CrushingWheelControllerBlockEntityMixin {
    @Shadow
    public abstract boolean hasEntity();

    @Unique
    private int create_horse_decimation$tickTimerDetection = 0;

    @Unique
    private boolean create_horse_decimation$isItemCrushingDebounced = false;

    // dear developers of create who made the crushing wheel, more specifically the tick method
    // PLEASE SPLIT YOUR CODE AND STOP NESTING IF STATEMENTS
    // https://media.tenor.com/zrEcHfcgTNQAAAAe/alpha-wolf-alpha.png
    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/processing/recipe/ProcessingInventory;getStackInSlot(I)Lnet/minecraft/world/item/ItemStack;", ordinal = 3))
    private ItemStack onEjectOutputItem(ProcessingInventory instance, int i, Operation<ItemStack> original) {
        var outputStack = original.call(instance, i);
        if (outputStack.isEmpty()) return ItemStack.EMPTY;

        onItemCrush(outputStack);

        return outputStack;
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/processing/recipe/ProcessingInventory;getStackInSlot(I)Lnet/minecraft/world/item/ItemStack;", ordinal = 2))
    private ItemStack onAddingOutputItemOnBelt(ProcessingInventory instance, int i, Operation<ItemStack> original) {
        var outputStack = original.call(instance, i);
        if (outputStack.isEmpty()) return ItemStack.EMPTY;

        onItemCrush(outputStack);

        return outputStack;
    }

    private void onItemCrush(ItemStack outputStack) {
        if (create_horse_decimation$isItemCrushingDebounced) return;
        if (!outputStack.is(ModItems.HORSE_DUST)) return;

        var self = getSelf();
        var pos = self.getBlockPos();
        var level = self.getLevel();
        if (!(level instanceof ServerLevel serverLevel)) return;

        create_horse_decimation$isItemCrushingDebounced = true;
        serverLevel.playSound(
                null,
                pos.getX(),
                pos.getY(),
                pos.getZ(),
                SoundEvents.HORSE_DEATH,
                SoundSource.BLOCKS,
                1.0f,
                1.0f);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void injectEquineDetection(CallbackInfo ci) {
        create_horse_decimation$isItemCrushingDebounced = false;
        if (hasEntity()) return;

        int tickDelay = Config.server.ticksPerHorseDetection.get();
        if (this.create_horse_decimation$tickTimerDetection <= tickDelay) {
            this.create_horse_decimation$tickTimerDetection++;
            return;
        } else this.create_horse_decimation$tickTimerDetection = 1;

        CrushingWheelControllerBlockEntity self = getSelf();
        Level level = self.getLevel();
        BlockPos blockPos = self.getBlockPos();
        if (level == null) return;

        AABB detectionBox = new AABB(blockPos).inflate(0.2);

        var jorseList = level.getEntitiesOfClass(Horse.class, detectionBox);
        if (jorseList.isEmpty()) return;
        var jorse = jorseList.getFirst();

        var health = jorse.getAttribute(Attributes.MAX_HEALTH);
        if (health == null) return;
        health.setBaseValue(8f);

        self.startCrushing(jorse);
    }

    @Unique
    public CrushingWheelControllerBlockEntity getSelf() {
        return (CrushingWheelControllerBlockEntity) (Object) this;
    }
}
