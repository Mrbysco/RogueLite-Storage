package com.mrbysco.rlstorage;

import com.mrbysco.rlstorage.storage.SafeDataStorage;
import net.minecraft.server.MinecraftServer;
import net.neoforged.fml.loading.FMLPaths;

import java.io.File;

public class Reference {
	public static SafeDataStorage safeDataStorage;
	private static final File storageFolder = new File(FMLPaths.GAMEDIR.get().toFile() + "/roguelitestorage");

	public static SafeDataStorage getVaultDataStorage(MinecraftServer server) {
		if (safeDataStorage == null || !storageFolder.exists()) {
			storageFolder.mkdirs();
			return safeDataStorage = new SafeDataStorage(storageFolder, server.getFixerUpper());
		} else {
			return safeDataStorage;
		}
	}
}
