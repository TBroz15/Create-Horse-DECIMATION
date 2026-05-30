package dev.tuxebro.create_horse_decimation.event;

import dev.tuxebro.create_horse_decimation.CreateHorseDecimation;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.Horse;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

// horse abductors suck horses really fast
// so minecraft registers horses flying at high speeds like elytras for some reason
@EventBusSubscriber
public class PreventHorseDeathOnHighVelocitiesHandler {
    @SubscribeEvent
    public static void onLivingFall(LivingDamageEvent.Pre event) {
        if (!(event.getSource().is(DamageTypes.FLY_INTO_WALL))) return;

        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Horse)) return;

        event.setNewDamage(0);
    }
}
