package com.mrbysco.rlstorage.menu;

import com.mrbysco.rlstorage.registry.RLRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;

public class SafeMenu extends ChestMenu {
	private final Player player;

	public SafeMenu(int i, Inventory playerInventory, FriendlyByteBuf buffer) {
		this(i, playerInventory, new SimpleContainer(9));
	}

	public SafeMenu(int containerId, Inventory playerInventory, Container container) {
		super(RLRegistry.SAFE_MENU.get(), containerId, playerInventory, container, 1);
		this.player = playerInventory.player;
	}

	@Override
	public void removed(Player player) {
		super.removed(player);
	}

	@Override
	public void slotsChanged(Container container) {
		super.slotsChanged(container);
	}
}
