package com.mrbysco.rlstorage.registry;

import com.mrbysco.rlstorage.RogueLiteStorage;
import com.mrbysco.rlstorage.block.SafeBlock;
import com.mrbysco.rlstorage.block.entity.SafeBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.Builder;
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
			new SafeBlock(Properties.copy(Blocks.ANVIL).requiresCorrectToolForDrops()
					.strength(5.0F, 1200.0F).sound(SoundType.ANVIL)));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SafeBlockEntity>> SAFE_BLOCK_ENTITY = BLOCK_ENTITIES.register("safe", () ->
			Builder.of(SafeBlockEntity::new, SAFE.get()).build(null));
	public static final DeferredItem<BlockItem> SAFE_ITEM = ITEMS.register("safe", () ->
			new BlockItem(SAFE.get(), (new Item.Properties())));
}
