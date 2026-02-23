package dev.tamago0314.astmoss.client.logic;

import dev.tamago0314.astmoss.client.AstmossClient;
import dev.tamago0314.astmoss.client.util.BlockMatcher;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public class AutoMossHandler {

    private static int useCooldown = 0;

    public static void tick(MinecraftClient client) {

        if (!AstmossClient.ENABLED) return;
        if (client.player == null || client.world == null) return;

        ClientPlayerEntity player = client.player;

        if (useCooldown > 0) {
            useCooldown--;
        }

        if (!(client.crosshairTarget instanceof BlockHitResult hit)) return;
        if (hit.getType() != HitResult.Type.BLOCK) return;

        BlockPos pos = hit.getBlockPos();

        if (BlockMatcher.shouldBreak(client.world.getBlockState(pos))) {

            if (client.interactionManager != null) {
                client.interactionManager.attackBlock(pos, hit.getSide());
                player.swingHand(Hand.MAIN_HAND);
            }

            return;
        }

        if (BlockMatcher.isMossBlock(client.world.getBlockState(pos))) {

            if (useCooldown > 0) return;

            Hand hand = InventoryHelper.getBonemealHand(player);

            if (hand != null && client.interactionManager != null) {

                client.interactionManager.interactBlock(
                        player,
                        hand,
                        hit
                );

                player.swingHand(hand);
                useCooldown = 4;
            }
        }

        InventoryHelper.refillBonemeal(player);
    }
}