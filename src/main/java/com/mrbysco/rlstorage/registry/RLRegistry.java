package com.mrbysco.rlstorage.registry;

import com.mrbysco.rlstorage.RogueLiteStorage;
import com.mrbysco.rlstorage.block.SafeBlock;
import com.mrbysco.rlstorage.block.entity.SafeBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RLRegistry {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(RogueLiteStorage.MOD_ID);
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RogueLiteStorage.MOD_ID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, RogueLiteStorage.MOD_ID);
	public static final DeferredBlock<SafeBlock> SAFE = BLOCKS.register("safe", () ->
			new SafeBlock(Properties.ofFullCopy(Blocks.ANVIL).setId(blockKey("safe")).requiresCorrectToolForDrops()
					.strength(5.0F, 1200.0F).sound(SoundType.ANVIL)));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SafeBlockEntity>> SAFE_BLOCK_ENTITY = BLOCK_ENTITIES.register("safe", () ->
			new BlockEntityType<>(SafeBlockEntity::new, SAFE.get()));
	public static final DeferredItem<BlockItem> SAFE_ITEM = ITEMS.registerSimpleBlockItem(SAFE);

	private static ResourceKey<Block> blockKey(String path) {
		return ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(RogueLiteStorage.MOD_ID, path));
	}
}
