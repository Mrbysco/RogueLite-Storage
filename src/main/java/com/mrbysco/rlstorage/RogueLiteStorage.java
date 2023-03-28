package com.mrbysco.rlstorage;

import com.mojang.logging.LogUtils;
import com.mrbysco.rlstorage.registry.RLRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod("roguelitestorage")
public class RogueLiteStorage {
	public static final String MOD_ID = "roguelitestorage";
	public static final Logger LOGGER = LogUtils.getLogger();

	public RogueLiteStorage() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		RLRegistry.BLOCKS.register(eventBus);
		RLRegistry.BLOCK_ENTITIES.register(eventBus);
		RLRegistry.ITEMS.register(eventBus);

		eventBus.addListener(this::buildCreativeContents);
	}

	private void buildCreativeContents(CreativeModeTabEvent.BuildContents event) {
		var entries = event.getEntries();
		var visibility = CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;
		if (event.getTab() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
			entries.put(new ItemStack(RLRegistry.SAFE_ITEM.get()), visibility);
		}
	}
}
