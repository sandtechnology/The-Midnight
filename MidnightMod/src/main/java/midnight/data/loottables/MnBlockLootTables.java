package midnight.data.loottables;

import midnight.common.block.MnBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class MnBlockLootTables extends BlockLootTables {

    protected static LootTable.Builder droppingNothing() {
        return LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(0)));
    }

    @Override
    protected void addTables() {
        registerDropSelfLootTable(MnBlocks.NIGHT_STONE);
        registerDropSelfLootTable(MnBlocks.NIGHT_DIRT);
        registerDropSelfLootTable(MnBlocks.DECEITFUL_PEAT);
        registerDropSelfLootTable(MnBlocks.DECEITFUL_MUD);
        registerDropSelfLootTable(MnBlocks.TRENCHSTONE);
        registerDropSelfLootTable(MnBlocks.STRANGE_SAND);
        registerDropSelfLootTable(MnBlocks.COARSE_NIGHT_DIRT);
        registerDropSelfLootTable(MnBlocks.GHOST_PLANT_LEAF);
        registerDropSelfLootTable(MnBlocks.GHOST_PLANT_STEM);
        registerDropSelfLootTable(MnBlocks.GHOST_PLANT);
        registerSilkTouch(MnBlocks.NIGHT_BEDROCK);
        registerLootTable(MnBlocks.NIGHT_GRASS, onlyWithShears(MnBlocks.NIGHT_GRASS));
        registerLootTable(MnBlocks.TALL_NIGHT_GRASS, onlyWithShears(MnBlocks.TALL_NIGHT_GRASS));
        registerLootTable(MnBlocks.NIGHT_GRASS_BLOCK, block -> droppingWithSilkTouch(block, MnBlocks.NIGHT_DIRT));
        registerLootTable(MnBlocks.DARK_WATER, block -> droppingNothing());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS.getValues()
                                     .stream()
                                     .filter(block -> {
                                         ResourceLocation loc = block.getRegistryName();
                                         assert loc != null;
                                         return loc.getNamespace().equals("midnight");
                                     })::iterator;
    }
}
