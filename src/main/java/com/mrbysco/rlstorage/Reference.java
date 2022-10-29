package com.mrbysco.rlstorage;

import com.mrbysco.rlstorage.storage.SafeDataStorage;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;

public class Reference {
	public static SafeDataStorage safeDataStorage;

	public static SafeDataStorage getVaultDataStorage(MinecraftServer server) {
		if (safeDataStorage == null) {
			File file1 = new File(FMLPaths.GAMEDIR.get().toFile() + "/roguelitestorage");
			file1.mkdirs();
			return safeDataStorage = new SafeDataStorage(file1, server.getFixerUpper());
		} else {
			return safeDataStorage;
		}
	}
}
