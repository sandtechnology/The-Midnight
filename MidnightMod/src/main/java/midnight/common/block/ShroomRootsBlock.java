/*
 * Copyright (c) 2020 Cryptic Mushroom and contributors
 * This file belongs to the Midnight mod and is licensed under the terms and conditions of Cryptic Mushroom. See
 * https://github.com/Cryptic-Mushroom/The-Midnight/blob/rewrite/LICENSE.md for the full license.
 *
 * Last updated: 2020 - 10 - 21
 */

package midnight.common.block;

import midnight.common.misc.tags.MnBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class ShroomRootsBlock extends HangingPlantBlock {
    protected ShroomRootsBlock(Properties props) {
        super(props);
    }

    @Override
    protected boolean isValidGround(BlockState state, IBlockReader world, BlockPos pos) {
        return state.isIn(MnBlockTags.SHROOM_STEMS)
                   || state.isIn(MnBlockTags.SHROOM_CAPS)
                   || state.isIn(MnBlockTags.SHROOM_PLANKS);
    }
}
