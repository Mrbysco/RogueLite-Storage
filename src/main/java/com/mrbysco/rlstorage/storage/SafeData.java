package com.mrbysco.rlstorage.storage;

import com.mrbysco.rlstorage.Reference;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class SafeData extends SavedData {
	private static final String DATA_NAME = "roguelitestorage_safe_data";
	private Map safeMap;

	public SafeData() {
		this(new HashMap());
	}

	public SafeData(Map safeMap) {
		this.safeMap = new HashMap();
		this.safeMap = safeMap;
	}

	public static SafeData load(CompoundTag tag) {
		ListTag baseChestsList = tag.getList("safes", 10);
		Map safeMap = new HashMap();

		for (int i = 0; i < baseChestsList.size(); ++i) {
			CompoundTag listTag = baseChestsList.getCompound(i);
			UUID uuid = listTag.getUUID("Owner");
			int chestSize = listTag.getInt("SafeSize");
			ListTag chestTag = listTag.getList("Safe", 10);
			SafeInventory inventory = new SafeInventory(chestSize);
			inventory.fromTag(chestTag);
			safeMap.put(uuid, inventory);
		}

		return new SafeData(safeMap);
	}

	public CompoundTag save(CompoundTag tag) {
		ListTag safeList = new ListTag();
		Iterator var3 = this.safeMap.entrySet().iterator();

		while (var3.hasNext()) {
			Map.Entry entry = (Map.Entry) var3.next();
			CompoundTag baseChestsTag = new CompoundTag();
			baseChestsTag.putUUID("Owner", (UUID) entry.getKey());
			baseChestsTag.putInt("SafeSize", ((SafeInventory) entry.getValue()).getContainerSize());
			baseChestsTag.put("Safe", ((SafeInventory) entry.getValue()).of());
			safeList.add(baseChestsTag);
		}

		tag.put("safes", safeList);
		return tag;
	}

	public SafeInventory getInventoryFromUUID(UUID uuid) {
		return this.safeMap.containsKey(uuid) ? (SafeInventory) this.safeMap.get(uuid) : (SafeInventory) this.safeMap.put(uuid, new SafeInventory(9));
	}

	public void setDirty() {
		super.setDirty();
		if (Reference.safeDataStorage != null) {
			Reference.safeDataStorage.save();
		}

	}

	public static SafeData get(Level level) {
		if (!(level instanceof ServerLevel)) {
			throw new RuntimeException("Attempted to get the data from a client level. This is wrong.");
		} else {
			return (SafeData) Reference.getVaultDataStorage(level.getServer()).computeIfAbsent(SafeData::load, SafeData::new, "roguelitestorage_safe_data");
		}
	}
}
