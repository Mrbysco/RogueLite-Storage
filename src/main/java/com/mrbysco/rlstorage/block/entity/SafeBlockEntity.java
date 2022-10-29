package com.mrbysco.rlstorage.block.entity;

import com.mrbysco.rlstorage.registry.RLRegistry;
import com.mrbysco.rlstorage.storage.SafeData;
import com.mrbysco.rlstorage.storage.SafeInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class SafeBlockEntity extends BlockEntity {
	public SafeBlockEntity(BlockPos pos, BlockState state) {
		super((BlockEntityType) RLRegistry.SAFE_BLOCK_ENTITY.get(), pos, state);
	}

	public Component getDisplayName() {
		return new TranslatableComponent("roguelitestorage.container.safe");
	}

	public boolean stillValid(Player player) {
		if (this.level.getBlockEntity(this.worldPosition) != this) {
			return false;
		} else {
			return !(player.distanceToSqr(
					(double)this.worldPosition.getX() + 0.5D,
					(double)this.worldPosition.getY() + 0.5D,
					(double)this.worldPosition.getZ() + 0.5D) > 64.0D);
		}
	}

	public SafeInventory getInventory(UUID uuid, Level level) {
		return level.isClientSide ? null : this.getVaultData(level).getInventoryFromUUID(uuid);
	}

	public SafeData getVaultData(Level level) {
		SafeData data = SafeData.get(level);
		data.setDirty();
		return data;
	}
}
