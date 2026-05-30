package dev.tuxebro.create_horse_decimation.block.horse_abductor;

import dev.tuxebro.create_horse_decimation.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class HorseAbductorInventoryHandler extends ItemStackHandler implements IItemHandler {
    private boolean isContentChanged = false;
    private final Runnable setChangeRunnable;
    private int occupiedSlots = 0;

    public HorseAbductorInventoryHandler(int maxSlots, Runnable setChangeRunnable) {
        super(maxSlots);
        this.setChangeRunnable = setChangeRunnable;
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (!ModItems.HORSE_JPG.isIn(stack)) return stack;

        ItemStack previous = getStackInSlot(slot);
        ItemStack remainder = super.insertItem(slot, stack, simulate);

        if (simulate) return remainder;
        if (previous.isEmpty() && remainder.isEmpty()) {
            occupiedSlots++;
        }

        return remainder;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack previous = getStackInSlot(slot);
        ItemStack extracted = super.extractItem(slot, amount, simulate);

        if (simulate) return extracted;
        if (!previous.isEmpty() && getStackInSlot(slot).isEmpty()) {
            occupiedSlots--;
        }

        return extracted;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        ItemStack previous = getStackInSlot(slot);
        super.setStackInSlot(slot, stack);

        if (previous.isEmpty() && !stack.isEmpty()) {
            occupiedSlots++;
        } else if (!previous.isEmpty() && stack.isEmpty()) {
            occupiedSlots--;
        }
    }

    @Override
    protected void onContentsChanged(int slot) {
        this.isContentChanged = true;
    }

    public void checkOnChange() {
        if (!this.isContentChanged) return;

        this.isContentChanged = false;
        this.setChangeRunnable.run();
    }

    public boolean isFull() {
        return this.occupiedSlots >= getSlots();
    }

    public int getOccupiedSlots() {
        return this.occupiedSlots;
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        recalcSlots();
    }

    public void recalcSlots() {
        this.occupiedSlots = 0;
        for (int i = 0; i < getSlots(); i++) {
            if (getStackInSlot(i).isEmpty()) continue;
            this.occupiedSlots++;
        }
    }

    public void dropInv(Level level, BlockPos pos) {
        if (level == null) return;
        if (level.isClientSide()) return;

        for (int i = 0; i < this.getSlots(); i++) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), this.getStackInSlot(i));
        }
    }
}
