package com.mrbysco.rlstorage.storage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.rlstorage.Reference;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SafeData extends SavedData {
	private static final String DATA_NAME = "roguelitestorage_safe_data";

	public static final Codec<SafeData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
					Codec.unboundedMap(UUIDUtil.STRING_CODEC, SafeInventory.CODEC).fieldOf("infoMap")
							.forGetter(data -> data.safeMap))
			.apply(inst, SafeData::new));


	private final Map<UUID, SafeInventory> safeMap;

	public SafeData() {
		this(new HashMap<>());
	}

	public SafeData(Map<UUID, SafeInventory> rawMap) {
		this.safeMap = new HashMap<>(rawMap);
		this.setDirty();
	}

	public SafeInventory getInventoryFromUUID(UUID uuid) {
		return this.safeMap.computeIfAbsent(uuid, k -> new SafeInventory(9));
	}

	public void setDirty() {
		super.setDirty();
	}

	public static SavedDataType<SafeData> type() {
		return new SavedDataType<>(DATA_NAME, SafeData::new, CODEC, null);
	}

	public static SafeData get(Level level) {
		if (!(level instanceof ServerLevel)) {
			throw new RuntimeException("Attempted to get the data from a client world. This is wrong.");
		}
		ServerLevel overworld = level.getServer().getLevel(Level.OVERWORLD);

		return Reference.getVaultDataStorage(overworld).computeIfAbsent(type());
	}
}
