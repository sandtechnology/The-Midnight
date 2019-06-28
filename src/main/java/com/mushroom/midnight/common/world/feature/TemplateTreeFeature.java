package com.mushroom.midnight.common.world.feature;

import com.mojang.datafixers.Dynamic;
import com.mushroom.midnight.Midnight;
import com.mushroom.midnight.common.block.MidnightFungiHatBlock;
import com.mushroom.midnight.common.block.MidnightFungiStemBlock;
import com.mushroom.midnight.common.util.WorldUtil;
import com.mushroom.midnight.common.world.feature.config.TemplateTreeConfig;
import com.mushroom.midnight.common.world.template.CompiledTemplate;
import com.mushroom.midnight.common.world.template.RotatedSettingConfigurator;
import com.mushroom.midnight.common.world.template.TemplateCompiler;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.LogBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.template.Template;

import java.util.Collection;
import java.util.Random;
import java.util.function.Function;

public class TemplateTreeFeature extends MidnightTreeFeature<TemplateTreeConfig> {
    private static final String ANCHOR_KEY = "origin";
    private static final String TRUNK_TOP_KEY = "trunk_top";
    private static final String TRUNK_CORNER_KEY = "trunk_corner";

    private TemplateCompiler templateCompiler;

    public TemplateTreeFeature(Function<Dynamic<?>, ? extends TemplateTreeConfig> deserialize) {
        super(deserialize);
    }

    protected TemplateCompiler buildCompiler(TemplateTreeConfig config) {
        return TemplateCompiler.of(config.templates)
                .withAnchor(ANCHOR_KEY)
                .withSettingConfigurator(RotatedSettingConfigurator.INSTANCE)
                .withProcessor((world, pos, srcInfo, info, settings) -> this.processState(pos, info, config))
                .withDataProcessor((world, pos, key) -> this.processMarker(world, pos, key, config));
    }

    @Override
    protected boolean place(IWorld world, Random random, BlockPos origin, TemplateTreeConfig config) {
        if (!isSoil(world, origin, this.getSapling())) {
            return false;
        }

        if (this.templateCompiler == null) {
            this.templateCompiler = this.buildCompiler(config);
        }

        CompiledTemplate template = this.templateCompiler.compile(world, random, origin);

        BlockPos trunkTop = template.lookupAny(TRUNK_TOP_KEY);
        Collection<BlockPos> trunkCorners = template.lookup(TRUNK_CORNER_KEY);
        if (trunkTop == null || trunkCorners.isEmpty()) {
            Midnight.LOGGER.warn("Template '{}' did not have required '{}' and '{}' data blocks", template, TRUNK_TOP_KEY, TRUNK_CORNER_KEY);
            return false;
        }

        BlockPos minCorner = WorldUtil.min(trunkCorners).add(1, 0, 1);
        BlockPos maxCorner = WorldUtil.max(trunkCorners).add(-1, 0, -1);

        if (!this.canGrow(world, minCorner, maxCorner) || !this.canFit(world, trunkTop, minCorner, maxCorner)) {
            return false;
        }

        this.setDirtAt(world, origin.down(), origin);

        template.addTo(world, random, 2 | 16);

        return true;
    }

    protected boolean canGrow(IWorld world, BlockPos minCorner, BlockPos maxCorner) {
        for (BlockPos pos : BlockPos.getAllInBoxMutable(minCorner, maxCorner)) {
            if (!isSoil(world, pos, this.getSapling())) {
                return false;
            }
        }
        return true;
    }

    protected boolean canFit(IWorld world, BlockPos trunkTop, BlockPos minCorner, BlockPos maxCorner) {
        BlockPos maxFit = new BlockPos(maxCorner.getX(), trunkTop.getY(), maxCorner.getZ());

        for (BlockPos pos : BlockPos.getAllInBoxMutable(minCorner, maxFit)) {
            if (!isAirOrLeaves(world, pos)) {
                return false;
            }
        }

        return true;
    }

    protected Template.BlockInfo processState(BlockPos pos, Template.BlockInfo info, TemplateTreeConfig config) {
        BlockState state = info.state;
        Block block = state.getBlock();
        if (block instanceof LogBlock) {
            Direction.Axis axis = state.get(LogBlock.AXIS);
            return new Template.BlockInfo(pos, config.log.with(LogBlock.AXIS, axis), null);
        } else if (block instanceof MidnightFungiStemBlock) {
            return new Template.BlockInfo(pos, config.log, null);
        } else if (block instanceof LeavesBlock || block instanceof MidnightFungiHatBlock) {
            return new Template.BlockInfo(pos, config.leaf, null);
        } else if (block == Blocks.STRUCTURE_BLOCK || block instanceof AirBlock) {
            return null;
        }
        return info;
    }

    protected void processMarker(IWorld world, BlockPos pos, String key, TemplateTreeConfig config) {
        if (key.equals(ANCHOR_KEY) || key.equals(TRUNK_TOP_KEY)) {
            this.setBlockState(world, pos, config.log);
        }
    }
}
