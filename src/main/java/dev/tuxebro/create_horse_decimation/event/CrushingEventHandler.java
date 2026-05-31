package dev.tuxebro.create_horse_decimation.event;

import com.simibubi.create.AllDamageTypes;
import dev.tuxebro.create_horse_decimation.CreateHorseDecimation;
import dev.tuxebro.create_horse_decimation.ModItems;
import dev.tuxebro.create_horse_decimation.config.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;


import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

@EventBusSubscriber(modid = CreateHorseDecimation.MOD_ID)
public class CrushingEventHandler {
    private static final ConcurrentHashMap<Player, AtomicInteger> playersToSmite = new ConcurrentHashMap<>();

    @SubscribeEvent
    public static void onTickSmite(LevelTickEvent.Post event) {
        if (event.getLevel().isClientSide) return;
        if (playersToSmite.isEmpty()) return;

        playersToSmite.forEach((player, atomicTicks) -> {
            int ticks = atomicTicks.decrementAndGet();

            if (ticks <= 0) {
                playersToSmite.remove(player);
                return;
            }

            if (ticks % 2 == 0) return;

            EntityType.LIGHTNING_BOLT.spawn((ServerLevel) event.getLevel(), BlockPos.containing(player.position()), MobSpawnType.EVENT);
            player.invulnerableTime = 0;
        });
    }

    public static void yeetAndSmiteNearestPlayers(Vec3 pos, Level level) {
        if (Config.server.survivalFriendlyEnabled.get()) return;
        var area = new AABB(BlockPos.containing(pos)).inflate(20);

        List<Player> search = level.getEntitiesOfClass(Player.class, area);
        if (search.isEmpty()) return;
        if (level.isClientSide) return;

        search.forEach((player) -> {
            // TODO: Make the code linear and cuz ts sucks lmao but it works so dw alot
            TickHandler.addDelayedTask(5, ()->{
                var serverPlayer = (ServerPlayer) player;

                serverPlayer.connection.send(
                        new ClientboundSetTitlesAnimationPacket(5, 80, 5));

                serverPlayer.connection.send(new ClientboundSetSubtitleTextPacket(
                        Component.literal("DEMISE UPON YOU").withStyle(ChatFormatting.RED, ChatFormatting.BOLD)));

                serverPlayer.connection.send(new ClientboundSetTitleTextPacket(Component.literal(" ")));

                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WITHER_SPAWN, SoundSource.NEUTRAL, 1.0F, 2.0F);

                TickHandler.addDelayedTask(10, ()->{
                    player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 10,127));
                    TempKeepInventoryHandler.players.add(player.getUUID());

                    TickHandler.addDelayedTask(25, ()-> playersToSmite.put(player, new AtomicInteger(101)));
                });
            });
        });
    }

    @SubscribeEvent
    public static void onHorseCrushed(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();

        if (!event.getSource().is(AllDamageTypes.CRUSH))
            return;

        if (entity instanceof Wolf dawg) {
            yeetAndSmiteNearestPlayers(dawg.position(), dawg.level());
            return;
        }

        if (!(entity instanceof Horse jorse))
            return;

        event.getDrops().clear();

        var entityBlockPos = entity.blockPosition();
        var fixedPos = Vec3.atBottomCenterOf(entityBlockPos);

        var horseDustStack = new ItemStack(ModItems.HORSE_DUST.get(), 1);
        ItemEntity horseDustEntity = new ItemEntity(
                jorse.level(),
                fixedPos.x,
                fixedPos.y,
                fixedPos.z,
                horseDustStack
        );
        horseDustEntity.setDeltaMovement(0.0, -0.2, 0.0);
        event.getDrops().add(horseDustEntity);
    }
}
