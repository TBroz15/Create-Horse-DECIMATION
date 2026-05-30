package dev.tuxebro.create_horse_decimation;

import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.AllFluids;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.entry.FluidEntry;
import dev.tuxebro.create_horse_decimation.block.horse_glue_fluid.HorseGlueFluidBlock;
import net.createmod.catnip.theme.Color;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Vector3f;

import java.util.List;
import java.util.function.Supplier;

public class ModFluids {
    private static final CreateRegistrate REGISTRATE = CreateHorseDecimation.registrate();

    static {
        REGISTRATE.setCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB);
    }

    public static final FluidEntry<BaseFlowingFluid.Flowing> HORSE_GLUE = REGISTRATE.standardFluid("horse_glue",
                    ModFluids.SolidRenderedPlaceableFluidType.create(0xF6FFFB, () -> 0xF6FFFBE8, () -> 1f / 16f))
            .lang("Horse Glue Fluid")
            .properties(b -> b.viscosity(2000)
                    .density(2000)
                    .canDrown(true)
                    .canSwim(true)
                    .canHydrate(true))
            .fluidProperties(p -> p.levelDecreasePerBlock(2)
                    .tickRate(25)
                    .slopeFindDistance(3)
                    .explosionResistance(100f))
            .source(BaseFlowingFluid.Source::new) // TODO: remove when Registrate fixes FluidBuilder
            .block(HorseGlueFluidBlock::new)
            .properties(p -> p.mapColor(MapColor.TERRACOTTA_WHITE))
            .build()
            .bucket()
            .properties(p -> p.component(DataComponents.LORE, new ItemLore(List.of(
                        Component.literal("nut what you think it is")
            ))))
            .onRegister(ModFluids::registerFluidDispenseBehavior)
            .build()
            .register();

    public static void register() {
    }

    private static final DispenseItemBehavior DEFAULT = new DefaultDispenseItemBehavior();
    private static final DispenseItemBehavior DISPENSE_FLUID = new DefaultDispenseItemBehavior(){
        @Override
        protected ItemStack execute(BlockSource pSource, ItemStack pStack) {
            DispensibleContainerItem dispensibleContainerItem = (DispensibleContainerItem) pStack.getItem();
            BlockPos pos = pSource.pos().relative(pSource.state().getValue(DispenserBlock.FACING));
            Level level = pSource.level();
            if (dispensibleContainerItem.emptyContents(null, level, pos, null, pStack)) {
                return new ItemStack(Items.BUCKET);
            }
            return DEFAULT.dispense(pSource, pStack);
        }
    };
    private static void registerFluidDispenseBehavior(BucketItem bucket) {
        DispenserBlock.registerBehavior(bucket, DISPENSE_FLUID);
    }

    private static class SolidRenderedPlaceableFluidType extends AllFluids.TintedFluidType {
        private Vector3f fogColor;
        private Supplier<Float> fogDistance;

        private Supplier<Integer> tintColor;

        public static FluidBuilder.FluidTypeFactory create(int fogColor, Supplier<Integer> tintColor, Supplier<Float> fogDistance) {
            return (p, s, f) -> {
                ModFluids.SolidRenderedPlaceableFluidType fluidType = new ModFluids.SolidRenderedPlaceableFluidType(p, s, f);
                fluidType.fogColor = new Color(fogColor, false).asVectorF();
                fluidType.fogDistance = fogDistance;
                fluidType.tintColor = tintColor;
                return fluidType;
            };
        }

        private SolidRenderedPlaceableFluidType(Properties properties, ResourceLocation stillTexture,
                                                ResourceLocation flowingTexture) {
            super(properties, stillTexture, flowingTexture);
        }

        @Override
        protected int getTintColor(FluidStack stack) {
            return tintColor.get();
        }

        /*
         * Removing alpha from tint prevents optifine from forcibly applying biome
         * colors to modded fluids (this workaround only works for fluids in the solid
         * render layer)
         */
        @Override
        public int getTintColor(FluidState state, BlockAndTintGetter world, BlockPos pos) {
            return tintColor.get();
        }

        @Override
        protected Vector3f getCustomFogColor() {
            return fogColor;
        }

        @Override
        protected float getFogDistanceModifier() {
            return fogDistance.get();
        }

    }
}
