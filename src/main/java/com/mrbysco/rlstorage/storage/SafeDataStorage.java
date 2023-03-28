package com.mrbysco.rlstorage.storage;

import com.google.common.collect.Maps;
import com.mojang.datafixers.DataFixer;
import com.mojang.logging.LogUtils;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.common.util.DummySavedData;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class SafeDataStorage {
	private static final Logger LOGGER = LogUtils.getLogger();
	private final Map<String, SavedData> cache = Maps.newHashMap();
	private final DataFixer fixerUpper;
	private final File dataFolder;

	public SafeDataStorage(File file, DataFixer dataFixer) {
		this.fixerUpper = dataFixer;
		this.dataFolder = file;
	}

	private File getDataFile(String name) {
		return new File(this.dataFolder, name + ".dat");
	}

	public SavedData computeIfAbsent(Function<CompoundTag, SavedData> savedDataFunction, Supplier<SavedData> dataSupplier, String id) {
		SavedData data = this.get(savedDataFunction, id);
		if (data != null) {
			return data;
		} else {
			SavedData data1 = dataSupplier.get();
			this.set(id, data1);
			return data1;
		}
	}

	@Nullable
	public SavedData get(Function<CompoundTag, SavedData> savedDataFunction, String id) {
		SavedData saveddata = this.cache.get(id);
		if (saveddata == DummySavedData.DUMMY) {
			return null;
		} else {
			if (saveddata == null && !this.cache.containsKey(id)) {
				saveddata = this.readSavedData(savedDataFunction, id);
				this.cache.put(id, saveddata);
			} else if (saveddata == null) {
				this.cache.put(id, DummySavedData.DUMMY);
				return null;
			}

			return saveddata;
		}
	}

	@Nullable
	private SavedData readSavedData(Function<CompoundTag, SavedData> function, String id) {
		try {
			File file1 = this.getDataFile(id);
			if (file1.exists()) {
				CompoundTag compoundtag = this.readTagFromDisk(id, SharedConstants.getCurrentVersion().getWorldVersion());
				return function.apply(compoundtag.getCompound("data"));
			}
		} catch (Exception var5) {
			LOGGER.error("Error loading saved data: {}", id, var5);
		}

		return null;
	}

	public void set(String name, SavedData savedData) {
		this.cache.put(name, savedData);
	}

	public CompoundTag readTagFromDisk(String id, int levelVersion) throws IOException {
		File dataFile = this.getDataFile(id);
		FileInputStream inputStream = new FileInputStream(dataFile);

		CompoundTag compoundTag;
		try {
			PushbackInputStream pushbackinputstream = new PushbackInputStream(inputStream, 2);

			try {
				CompoundTag compoundtag;
				if (this.isGzip(pushbackinputstream)) {
					compoundtag = NbtIo.readCompressed(pushbackinputstream);
				} else {
					DataInputStream dataInputStream = new DataInputStream(pushbackinputstream);

					try {
						compoundtag = NbtIo.read(dataInputStream);
					} catch (Throwable var14) {
						try {
							dataInputStream.close();
						} catch (Throwable var13) {
							var14.addSuppressed(var13);
						}

						throw var14;
					}

					dataInputStream.close();
				}

				int i = compoundtag.contains("DataVersion", 99) ? compoundtag.getInt("DataVersion") : 1343;
				compoundTag = NbtUtils.update(this.fixerUpper, DataFixTypes.SAVED_DATA, compoundtag, i, levelVersion);
			} catch (Throwable var15) {
				try {
					pushbackinputstream.close();
				} catch (Throwable var12) {
					var15.addSuppressed(var12);
				}

				throw var15;
			}

			pushbackinputstream.close();
		} catch (Throwable var16) {
			try {
				inputStream.close();
			} catch (Throwable var11) {
				var16.addSuppressed(var11);
			}

			throw var16;
		}

		inputStream.close();
		return compoundTag;
	}

	private boolean isGzip(PushbackInputStream inputStream) throws IOException {
		byte[] abyte = new byte[2];
		boolean flag = false;
		int i = inputStream.read(abyte, 0, 2);
		if (i == 2) {
			int j = (abyte[1] & 255) << 8 | abyte[0] & 255;
			if (j == 35615) {
				flag = true;
			}
		}

		if (i != 0) {
			inputStream.unread(abyte, 0, i);
		}

		return flag;
	}

	public void save() {
		this.cache.forEach((id, data) -> {
			if (data != null) {
				data.save(this.getDataFile(id));
			}

		});
	}
}
