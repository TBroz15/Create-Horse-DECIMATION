package dev.tuxebro.create_horse_decimation.datagen;

import com.simibubi.create.Create;
import com.simibubi.create.api.equipment.potatoCannon.PotatoCannonProjectileType;
import com.simibubi.create.api.registry.CreateRegistries;
import com.simibubi.create.content.equipment.potatoCannon.AllPotatoProjectileEntityHitActions;
import dev.tuxebro.create_horse_decimation.CreateHorseDecimation;
import dev.tuxebro.create_horse_decimation.ModItems;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffects;

public class ModPotatoProjectileTypes {
    public static void bootstrap(BootstrapContext<PotatoCannonProjectileType> ctx) {
        register(ctx, "horse_glue_ball_item", new PotatoCannonProjectileType.Builder()
                .damage(5)
                .reloadTicks(10)
                .velocity(1.25f)
                .knockback(1.5f)
                .renderTumbling()
                .onEntityHit(new AllPotatoProjectileEntityHitActions.PotionEffect(MobEffects.MOVEMENT_SLOWDOWN, 10, 160, true))
                .addItems(ModItems.HORSE_GLUE_BALL)
                .build());
    }

    private static void register(BootstrapContext<PotatoCannonProjectileType> ctx, String name, PotatoCannonProjectileType type) {
        ctx.register(ResourceKey.create(CreateRegistries.POTATO_PROJECTILE_TYPE, CreateHorseDecimation.asResource(name)), type);
    }
}
