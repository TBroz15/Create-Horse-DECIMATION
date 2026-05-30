package dev.tuxebro.create_horse_decimation.item;

import dev.tuxebro.create_horse_decimation.ModSoundEvents;
import dev.tuxebro.create_horse_decimation.config.Config;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class HorseDustItem extends Item {
    public HorseDustItem(Properties properties) {
        super(properties
                .food(new FoodProperties.Builder()
                        .nutrition(5)
                        .saturationModifier(5f)
                        .build()));
    }

    @Override
    public @NotNull SoundEvent getEatingSound() {
        return ModSoundEvents.SNIFFY_SNIFFA.get();
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entityLiving) {
        var itemAfterFinishing = super.finishUsingItem(stack, level, entityLiving);

        if (!(level instanceof ServerLevel serverLevel))
            return itemAfterFinishing;

        if (!(entityLiving instanceof ServerPlayer serverPlayer))
            return itemAfterFinishing;

        boolean hasSpeedAlready = serverPlayer.hasEffect(MobEffects.MOVEMENT_SPEED);

        if (hasSpeedAlready) {
            serverPlayer.removeAllEffects();
            serverPlayer.addEffect(new MobEffectInstance(MobEffects.POISON, 30 * 20, 255));
        } else
            serverPlayer.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 30 * 20, 5));

        return itemAfterFinishing;
    }


    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 64;
    }
}
