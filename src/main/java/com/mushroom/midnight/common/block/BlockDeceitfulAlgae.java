package com.mushroom.midnight.common.block;

import com.mushroom.midnight.client.IModelProvider;
import com.mushroom.midnight.common.registry.ModTabs;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;

import javax.annotation.Nullable;

public class BlockDeceitfulAlgae extends BlockBush implements IModelProvider {
    private static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 0.09375, 0.9375);

    public BlockDeceitfulAlgae() {
        this.setHardness(0.0F);
        this.setSoundType(SoundType.PLANT);
        this.setCreativeTab(ModTabs.DECORATION_TAB);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDS;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return null;
    }

    @Override
    protected boolean canSustainBush(IBlockState state) {
        Material material = state.getMaterial();
        return material == Material.WATER && state.getValue(BlockLiquid.LEVEL) == 0 || material == Material.ICE;
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Water;
    }
}
