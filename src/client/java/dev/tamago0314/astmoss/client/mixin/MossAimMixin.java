package dev.tamago0314.astmoss.client.mixin;

import dev.tamago0314.astmoss.client.AstmossClient;
import dev.tamago0314.astmoss.client.logic.InventoryHelper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MossAimMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void mossAimTick(CallbackInfo ci) {

        if (!AstmossClient.ENABLED) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;
        if (client.interactionManager == null) return;

        ClientPlayerEntity player = client.player;
        ClientPlayerInteractionManager interactionManager = client.interactionManager;
        World world = client.world;

        if (!(client.crosshairTarget instanceof BlockHitResult hit)) return;

        BlockPos pos = hit.getBlockPos();
        Block block = world.getBlockState(pos).getBlock();
        Direction dir = hit.getSide();

        boolean mainHas = player.getMainHandStack().isOf(net.minecraft.item.Items.BONE_MEAL);
        boolean offHas  = player.getOffHandStack().isOf(net.minecraft.item.Items.BONE_MEAL);

        if (block == Blocks.MOSS_BLOCK && (mainHas || offHas)) {

            // ★ 補充はInventoryHelperに完全委任
            InventoryHelper.refillBonemeal(player);

            Hand useHand = mainHas ? Hand.MAIN_HAND : Hand.OFF_HAND;

            Vec3d hitVec = hit.getPos();
            BlockHitResult bhr = new BlockHitResult(hitVec, dir, pos, false);

            interactionManager.interactBlock(player, useHand, bhr);
            player.swingHand(useHand);
            return;
        }

        if (block == Blocks.MOSS_CARPET) {
            interactionManager.updateBlockBreakingProgress(pos, dir);
            player.swingHand(Hand.MAIN_HAND);
            return;
        }

        if (block == Blocks.AZALEA
                || block == Blocks.FLOWERING_AZALEA
                || block == Blocks.GRASS
                || block == Blocks.TALL_GRASS) {

            interactionManager.attackBlock(pos, dir);
            player.swingHand(Hand.MAIN_HAND);
        }
    }
}