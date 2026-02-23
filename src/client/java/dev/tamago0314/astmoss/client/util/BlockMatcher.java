package dev.tamago0314.astmoss.client.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

public class BlockMatcher {

    public static boolean isMossBlock(BlockState state) {
        return state.isOf(Blocks.MOSS_BLOCK);
    }

    public static boolean shouldBreak(BlockState state) {

        return state.isOf(Blocks.GRASS)
                || state.isOf(Blocks.TALL_GRASS)
                || state.isOf(Blocks.AZALEA)
                || state.isOf(Blocks.FLOWERING_AZALEA)
                || state.isOf(Blocks.MOSS_CARPET);
    }
}