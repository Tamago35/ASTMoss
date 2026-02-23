package dev.tamago0314.astmoss.client.logic;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;

public class InventoryHelper {

    private static final int REFILL_THRESHOLD = 8;
    private static final int REFILL_AMOUNT = 32;

    private static final int WARNING_THRESHOLD = 128; // 2スタック
    private static boolean warningShown = false;

    public static Hand getBonemealHand(ClientPlayerEntity player) {

        if (player.getMainHandStack().isOf(Items.BONE_MEAL))
            return Hand.MAIN_HAND;

        if (player.getOffHandStack().isOf(Items.BONE_MEAL))
            return Hand.OFF_HAND;

        return null;
    }

    public static void refillBonemeal(ClientPlayerEntity player) {

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.interactionManager == null) return;
        if (!(player.currentScreenHandler instanceof PlayerScreenHandler handler)) return;

        checkBonemealWarning(player);

        Hand hand = getBonemealHand(player);
        if (hand == null) return;

        ItemStack handStack = hand == Hand.MAIN_HAND
                ? player.getMainHandStack()
                : player.getOffHandStack();

        if (handStack.getCount() > REFILL_THRESHOLD) return;

        int targetSlot = (hand == Hand.MAIN_HAND)
                ? 36 + player.getInventory().selectedSlot
                : 45;

        int needed = REFILL_AMOUNT - handStack.getCount();
        if (needed <= 0) return;

        int syncId = handler.syncId;

        for (int slot = 9; slot <= 44; slot++) {

            if (slot == targetSlot) continue;

            ItemStack stack = handler.getSlot(slot).getStack();

            if (stack.isOf(Items.BONE_MEAL) && stack.getCount() > 0) {

                client.interactionManager.clickSlot(
                        syncId, slot, 0,
                        SlotActionType.PICKUP, player
                );

                for (int i = 0; i < needed; i++) {
                    client.interactionManager.clickSlot(
                            syncId, targetSlot, 1,
                            SlotActionType.PICKUP, player
                    );
                }

                client.interactionManager.clickSlot(
                        syncId, slot, 0,
                        SlotActionType.PICKUP, player
                );

                break;
            }
        }
    }

    private static void checkBonemealWarning(ClientPlayerEntity player) {

        int total = 0;

        for (ItemStack stack : player.getInventory().main) {
            if (stack.isOf(Items.BONE_MEAL)) {
                total += stack.getCount();
            }
        }

        if (player.getOffHandStack().isOf(Items.BONE_MEAL)) {
            total += player.getOffHandStack().getCount();
        }

        if (total <= WARNING_THRESHOLD) {

            if (!warningShown) {
                player.sendMessage(
                        Text.literal("⚠ 手持ちの骨粉が残り2スタックです！")
                                .formatted(Formatting.RED),
                        false
                );
                warningShown = true;
            }

        } else {
            warningShown = false;
        }
    }
}