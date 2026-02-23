package dev.tamago0314.astmoss.client.key;

import dev.tamago0314.astmoss.client.AstmossClient;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {

    private static KeyBinding toggleKey;

    public static void register() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.astmoss.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "category.astmoss"
        ));
    }

    public static void handle(MinecraftClient client) {
        while (toggleKey.wasPressed()) {
            AstmossClient.ENABLED = !AstmossClient.ENABLED;

            if (client.player != null) {
                client.player.sendMessage(
                        Text.literal("Astmoss: " + (AstmossClient.ENABLED ? "ON" : "OFF")),
                        false
                );
            }
        }
    }
}