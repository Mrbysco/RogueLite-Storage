package com.mrbysco.rlstorage.client;

import com.mrbysco.rlstorage.client.screen.SafeScreen;
import com.mrbysco.rlstorage.registry.RLRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(Dist.CLIENT)
public class ClientHandler {
	@SubscribeEvent
	public static void registerMenuScreens(RegisterMenuScreensEvent event) {
		event.register(RLRegistry.SAFE_MENU.get(), SafeScreen::new);
	}
}
