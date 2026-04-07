package com.mrbysco.rlstorage;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.SavedDataStorage;
import net.neoforged.fml.loading.FMLPaths;

import java.io.File;

public class Reference {
	public static SavedDataStorage safeDataStorage;
	private static final File storageFolder = new File(FMLPaths.GAMEDIR.get().toFile() + "/roguelitestorage");

	public static SavedDataStorage getVaultDataStorage(ServerLevel level) {
		if (safeDataStorage == null || !storageFolder.exists()) {
			storageFolder.mkdirs();
			MinecraftServer server = level.getServer();
			return safeDataStorage = new SavedDataStorage(level, storageFolder.toPath(),
					server.getFixerUpper(), server.registryAccess());
		} else {
			return safeDataStorage;
		}
	}

	public static void saveData(boolean join) {
		if (safeDataStorage == null) return;
		if (join) {
			safeDataStorage.saveAndJoin();
		} else {
			safeDataStorage.scheduleSave();
		}
	}
}
