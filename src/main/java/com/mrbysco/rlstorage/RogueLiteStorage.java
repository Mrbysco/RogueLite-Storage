package com.mrbysco.rlstorage;

import com.mojang.logging.LogUtils;
import com.mrbysco.rlstorage.registry.RLRegistry;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
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

	private void buildCreativeContents(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
			event.accept(new ItemStack(RLRegistry.SAFE_ITEM.get()));
		}
	}
}
