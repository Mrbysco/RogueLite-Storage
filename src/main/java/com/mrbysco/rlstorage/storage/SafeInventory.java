package com.mrbysco.rlstorage.storage;

import com.google.common.collect.Iterables;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.rlstorage.block.entity.SafeBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Stream;

public class SafeInventory extends SimpleContainer {
	public static final SafeInventory EMPTY = new SafeInventory(NonNullList.create());
	public static final Codec<SafeInventory> CODEC = SafeInventory.Slot.CODEC
			.sizeLimitedListOf(9)
			.xmap(SafeInventory::fromSlots, SafeInventory::asSlots);

	private SafeBlockEntity associatedVault;
	private int hashCode;

	public SafeInventory(int size) {
		this(NonNullList.withSize(size, ItemStack.EMPTY));
	}

	public SafeInventory(NonNullList<ItemStack> items) {
		super(items.toArray(new ItemStack[0]));
		this.hashCode = ItemStack.hashStackList(items);
	}

	/**
	 * Update the hash code when the inventory is modified
	 */
	@Override
	public void setChanged() {
		super.setChanged();
		this.hashCode = ItemStack.hashStackList(items);
	}

	public void setAssociatedVault(SafeBlockEntity safeBlockEntity) {
		this.associatedVault = safeBlockEntity;
	}

	public boolean stillValid(Player player) {
		return (this.associatedVault == null || this.associatedVault.stillValid(player)) && super.stillValid(player);
	}

	public void startOpen(Player player) {
		super.startOpen(player);
	}

	public void stopOpen(Player player) {
		super.stopOpen(player);
		if (!player.level().isClientSide()) {
			SafeData.get(player.level()).setDirty();
		}

		this.associatedVault = null;
	}

	private static SafeInventory fromSlots(List<SafeInventory.Slot> slots) {
		OptionalInt optionalint = slots.stream().mapToInt(SafeInventory.Slot::index).max();
		if (optionalint.isEmpty()) {
			return EMPTY;
		} else {
			SafeInventory safeInventory = new SafeInventory(optionalint.getAsInt() + 1);

			for (SafeInventory.Slot itemcontainercontents$slot : slots) {
				safeInventory.items.set(itemcontainercontents$slot.index(), itemcontainercontents$slot.item());
			}

			return safeInventory;
		}
	}

	public static SafeInventory fromItems(List<ItemStack> items) {
		int i = findLastNonEmptySlot(items);
		if (i == -1) {
			return EMPTY;
		} else {
			SafeInventory safeInventory = new SafeInventory(i + 1);

			for (int j = 0; j <= i; j++) {
				safeInventory.items.set(j, items.get(j).copy());
			}

			return safeInventory;
		}
	}

	private static int findLastNonEmptySlot(List<ItemStack> items) {
		for (int i = items.size() - 1; i >= 0; i--) {
			if (!items.get(i).isEmpty()) {
				return i;
			}
		}

		return -1;
	}

	private List<SafeInventory.Slot> asSlots() {
		List<SafeInventory.Slot> list = new ArrayList<>();

		for (int i = 0; i < this.items.size(); i++) {
			ItemStack itemstack = this.items.get(i);
			if (!itemstack.isEmpty()) {
				list.add(new SafeInventory.Slot(i, itemstack));
			}
		}

		return list;
	}

	public void copyInto(NonNullList<ItemStack> list) {
		for (int i = 0; i < list.size(); i++) {
			ItemStack itemstack = i < this.items.size() ? this.items.get(i) : ItemStack.EMPTY;
			list.set(i, itemstack.copy());
		}
	}

	public ItemStack copyOne() {
		return this.items.isEmpty() ? ItemStack.EMPTY : this.items.getFirst().copy();
	}

	public Stream<ItemStack> stream() {
		return this.items.stream().map(ItemStack::copy);
	}

	public Stream<ItemStack> nonEmptyStream() {
		return this.items.stream().filter(p_331322_ -> !p_331322_.isEmpty()).map(ItemStack::copy);
	}

	public Iterable<ItemStack> nonEmptyItems() {
		return Iterables.filter(this.items, p_331420_ -> !p_331420_.isEmpty());
	}

	public Iterable<ItemStack> nonEmptyItemsCopy() {
		return Iterables.transform(this.nonEmptyItems(), ItemStack::copy);
	}

	@Override
	public boolean equals(Object other) {
		return this == other
				? true
				: other instanceof SafeInventory itemcontainercontents && ItemStack.listMatches(this.items, itemcontainercontents.items);
	}

	@Override
	public int hashCode() {
		return this.hashCode;
	}

	record Slot(int index, ItemStack item) {
		public static final Codec<SafeInventory.Slot> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
								Codec.intRange(0, 8).fieldOf("slot").forGetter(SafeInventory.Slot::index),
								ItemStack.CODEC.fieldOf("item").forGetter(SafeInventory.Slot::item)
						)
						.apply(instance, SafeInventory.Slot::new)
		);
	}
}
