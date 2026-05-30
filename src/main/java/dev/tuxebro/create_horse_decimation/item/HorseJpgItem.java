package dev.tuxebro.create_horse_decimation.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.core.appender.rolling.action.IfAll;

import java.util.List;
import java.util.Optional;

import static net.minecraft.commands.arguments.ResourceArgument.getEntityType;

public class HorseJpgItem extends Item {
    public HorseJpgItem(Properties properties) {
        super(properties
                .component(DataComponents.LORE, new ItemLore(List.of(
                        Component.literal("> walks in")
                )))
                .stacksTo(1));
    }

    public InteractionResult useOn(UseOnContext ctx) {
        var isReleased = release(
                ctx.getClickedPos(),
                ctx.getClickedFace(),
                ctx.getLevel(),
                ctx.getItemInHand());

        if (isReleased) return InteractionResult.SUCCESS;
        return InteractionResult.FAIL;
    }

    public static boolean release(BlockPos pos, Direction facing, Level level, ItemStack stack) {
        if (level.isClientSide) return false;

        stack.shrink(1);

        CustomData customData = stack.get(DataComponents.ENTITY_DATA);
        if (customData == null) {
            spawnJorse(pos, facing, level);
            return true;
        };

        CompoundTag entityTag = customData.copyTag();
        Optional<Entity> entityOptional = EntityType.create(entityTag, level);
        if (entityOptional.isEmpty()) {
            spawnJorse(pos, facing, level);
            return true;
        }

        var entity = entityOptional.get();
        spawnJorse(pos, facing, level, entity);
        return true;
    }

    public static void spawnJorse(BlockPos pos, Direction facing, Level level, Entity entity) {
        BlockPos blockPos = pos.relative(facing);

        level.playLocalSound(
                blockPos,
                SoundEvents.HORSE_ANGRY,
                SoundSource.NEUTRAL,
            1,1, false);

        entity.absMoveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 0, 0);
        level.addFreshEntity(entity);
    }

    public static void spawnJorse(BlockPos pos, Direction facing, Level level) {
        var entity = EntityType.HORSE.spawn((ServerLevel) level, pos, MobSpawnType.SPAWN_EGG);
        if (entity == null) return;

        spawnJorse(pos, facing, level, entity);
    }
}
