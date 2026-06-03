package dev.tuxebro.create_horse_decimation;

import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.tuxebro.create_horse_decimation.block.horse_abductor.HorseAbductorBlock;
import dev.tuxebro.create_horse_decimation.block.horse_block.HorseBlock;
import dev.tuxebro.create_horse_decimation.block.horse_door.SlidingHorseDoorBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.common.Tags;

import java.util.List;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

public class ModBlocks {
    private static final CreateRegistrate REGISTRATE = CreateHorseDecimation.registrate();

    static {
        REGISTRATE.setCreativeTab(ModCreativeModeTabs.MAIN_TAB);
    }

    public static final BlockEntry<HorseAbductorBlock> HORSE_ABDUCTOR_BLOCK = REGISTRATE.block("horse_abductor", HorseAbductorBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.PODZOL))
            .blockstate(BlockStateGen.directionalAxisBlockProvider())
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(axeOrPickaxe())
            .item()
            .transform(customItemModel())
            .lang("Horse Abductor 9000")
            .register();

    public static final BlockEntry<HorseBlock> HORSE_BLOCK = REGISTRATE.block("horse_block", HorseBlock::new)
            .blockstate(BlockStateGen.directionalBlockProvider(false))
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .item()
            .properties(p-> p.component(DataComponents.LORE, new ItemLore(List.of(
                    Component.literal("i dare you to break 7 thousand of these at once")
            ))))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .lang("Horse Block")
            .register();

    public static final BlockEntry<SlidingHorseDoorBlock> HORSE_DOOR = REGISTRATE.block("horse_door", SlidingHorseDoorBlock::new)
                    .properties(p -> p.mapColor(MapColor.STONE)
                            .noOcclusion())
                    .transform(BuilderTransformers.slidingDoor("horse"))
                    .register();

    public static void register() {}
}
