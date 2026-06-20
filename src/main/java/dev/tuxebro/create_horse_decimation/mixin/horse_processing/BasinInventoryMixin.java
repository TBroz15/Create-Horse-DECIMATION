package dev.tuxebro.create_horse_decimation.mixin.horse_processing;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.processing.basin.BasinInventory;
import dev.tuxebro.create_horse_decimation.ModItems;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BasinInventory.class)
public class BasinInventoryMixin {
    @WrapOperation(method = "insertItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isSameItemSameComponents(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private static boolean forcePutHorseJPGOnBasin(ItemStack stack, ItemStack other, Operation<Boolean> original) {
        if (stack.is(ModItems.HORSE_JPG))
            return false; // force insert

        return original.call(stack, other);
    }
}
