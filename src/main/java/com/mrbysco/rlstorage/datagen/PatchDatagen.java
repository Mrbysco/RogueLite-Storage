package com.mrbysco.rlstorage.datagen;

import com.mrbysco.rlstorage.RogueLiteStorage;
import com.mrbysco.rlstorage.registry.RLRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PatchDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();

		if (event.includeServer()) {
			generator.addProvider(event.includeServer(), new Loots(packOutput));
		}
		if (event.includeClient()) {
			generator.addProvider(event.includeClient(), new Language(packOutput));
		}
	}

	private static class Loots extends LootTableProvider {
		public Loots(PackOutput packOutput) {
			super(packOutput, Set.of(), List.of(
					new SubProviderEntry(PatchBlockLoot::new, LootContextParamSets.BLOCK)
			));
		}

		public static class PatchBlockLoot extends BlockLootSubProvider {
			protected PatchBlockLoot() {
				super(Set.of(), FeatureFlags.REGISTRY.allFlags());
			}

			@Override
			protected void generate() {
				dropSelf(RLRegistry.SAFE.get());
			}

			@Override
			protected Iterable<Block> getKnownBlocks() {
				return (Iterable<Block>) RLRegistry.BLOCKS.getEntries().stream().map(holder -> (Block) holder.get())::iterator;
			}
		}

		@Override
		protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationContext) {
			map.forEach((name, table) -> table.validate(validationContext));
		}
	}

	private static class Language extends LanguageProvider {
		public Language(PackOutput packOutput) {
			super(packOutput, RogueLiteStorage.MOD_ID, "en_us");
		}

		@Override
		protected void addTranslations() {
			this.addBlock(RLRegistry.SAFE, "Safe");
			this.add("roguelitestorage.container.safe", "Safe");
		}
	}
}
