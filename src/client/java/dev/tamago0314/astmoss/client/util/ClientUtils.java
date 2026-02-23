package dev.tamago0314.astmoss.client.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ClientUtils {

    public static void sendMessage(String message) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(Text.literal(message), false);
        }
    }
}