package com.mrbysco.rlstorage.registry;

import com.mojang.datafixers.types.Type;
import com.mrbysco.rlstorage.block.SafeBlock;
import com.mrbysco.rlstorage.block.entity.SafeBlockEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType.Builder;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RLRegistry {
	public static final DeferredRegister BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "roguelitestorage");
	public static final DeferredRegister ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "roguelitestorage");
	public static final DeferredRegister BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, "roguelitestorage");
	public static final RegistryObject SAFE = BLOCKS.register("safe", () ->
			new SafeBlock(Properties.of(Material.HEAVY_METAL, MaterialColor.METAL).requiresCorrectToolForDrops()
					.strength(5.0F, 1200.0F).sound(SoundType.ANVIL)));
	public static final RegistryObject SAFE_BLOCK_ENTITY = BLOCK_ENTITIES.register("safe", () ->
			Builder.of(SafeBlockEntity::new, new Block[]{(Block) SAFE.get()}).build((Type) null));
	public static final RegistryObject SAFE_ITEM = ITEMS.register("safe", () ->
			new BlockItem((Block) SAFE.get(), (new Item.Properties()).tab(CreativeModeTab.TAB_MISC)));
}
