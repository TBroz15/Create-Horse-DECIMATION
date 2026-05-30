package dev.tuxebro.create_horse_decimation.item;

import dev.tuxebro.create_horse_decimation.config.Config;
import dev.tuxebro.create_horse_decimation.event.TempKeepInventoryHandler;
import dev.tuxebro.create_horse_decimation.event.TickHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class HorseGlueBallItem extends Item {
    public HorseGlueBallItem(Properties properties) {
        super(properties
                .food(new FoodProperties.Builder()
                    .alwaysEdible()
                    .fast()
                    .nutrition(1)
                    .saturationModifier(1f)
                    .effect(() ->
                            new MobEffectInstance(MobEffects.POISON, 30 * 20, 255), 1.0F)
                    .build()
                )
        );
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entityLiving) {
        var itemAfterFinishing = super.finishUsingItem(stack, level, entityLiving);

        if (!(level instanceof ServerLevel serverLevel))
            return itemAfterFinishing;

        if (!(entityLiving instanceof ServerPlayer serverPlayer))
            return itemAfterFinishing;

        if (!Config.server.survivalFriendlyEnabled.get())
            smiteEm(serverLevel, serverPlayer);

        return itemAfterFinishing;
    }


    public static void smiteEm(ServerLevel level, ServerPlayer player) {
        player.setGameMode(GameType.SURVIVAL);

        TempKeepInventoryHandler.players.add(player.getUUID());

        TickHandler.addDelayedTask(32, () -> new ArrayList<>(Collections.nCopies(4, null)).forEach(item -> EntityType
                .LIGHTNING_BOLT
                .spawn(
                        (ServerLevel) level,
                        BlockPos.containing(player.position()),
                        MobSpawnType.EVENT)));

        TickHandler.addLimitedIntervalTasks(1,36, ()-> player.invulnerableTime = 0);
    }
}
