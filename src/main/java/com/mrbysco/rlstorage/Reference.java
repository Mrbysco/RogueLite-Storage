package com.mrbysco.rlstorage;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.neoforged.fml.loading.FMLPaths;

import java.io.File;

public class Reference {
	public static DimensionDataStorage safeDataStorage;
	private static final File storageFolder = new File(FMLPaths.GAMEDIR.get().toFile() + "/roguelitestorage");

	public static DimensionDataStorage getVaultDataStorage(MinecraftServer server) {
		if (safeDataStorage == null || !storageFolder.exists()) {
			storageFolder.mkdirs();
			return safeDataStorage = new DimensionDataStorage(storageFolder.toPath(), server.getFixerUpper(), server.registryAccess());
		} else {
			return safeDataStorage;
		}
	}
}
