package com.mushroom.midnight.common.world.feature;

import com.mushroom.midnight.common.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class HeapFeature extends MidnightAbstractFeature {
    private final IBlockState state;
    private int rangeXZ = 1, rangeY = 1;

    public HeapFeature(IBlockState state) {
        this.state = state;
    }

    @Override
    public boolean placeFeature(World world, Random random, BlockPos origin) {
        IBlockState belowState = world.getBlockState(origin.down());
        Block belowBlock = belowState.getBlock();
        if (belowBlock.isLeaves(belowState, world, origin.down()) || belowBlock == ModBlocks.NIGHTSHROOM_HAT || belowBlock == ModBlocks.DEWSHROOM_HAT || belowBlock == ModBlocks.VIRIDSHROOM_HAT || belowBlock == ModBlocks.TRENCHSTONE) {
            return false;
        }
        for (BlockPos pos : BlockPos.getAllInBox(origin.add(-rangeXZ, -rangeY, -rangeXZ), origin.add(rangeXZ, rangeY, rangeXZ))) {
            if (random.nextFloat() < 0.35f) {
                setBlockAndNotifyAdequately(world, pos, state);
            }
        }
        return true;
    }

    public HeapFeature withRange(int rangeXZ, int rangeY) {
        this.rangeXZ = rangeXZ;
        this.rangeY = rangeY;
        return this;
    }
}
