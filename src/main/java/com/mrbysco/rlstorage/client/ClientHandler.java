package com.mrbysco.rlstorage.client;

import com.mrbysco.rlstorage.client.screen.SafeScreen;
import com.mrbysco.rlstorage.registry.RLRegistry;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class ClientHandler {
	public static void registerMenuScreens(RegisterMenuScreensEvent event) {
		event.register(RLRegistry.SAFE_MENU.get(), SafeScreen::new);
	}
}
