package dev.tamago0314.astmoss.client;

import dev.tamago0314.astmoss.client.key.KeyBindings;
import dev.tamago0314.astmoss.client.logic.AutoMossHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class AstmossClient implements ClientModInitializer {

    public static final String MOD_ID = "astmoss";
    public static boolean ENABLED = false;

    @Override
    public void onInitializeClient() {

        KeyBindings.register();

        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);

        System.out.println("Astmoss Client Initialized.");
    }

    private void onClientTick(MinecraftClient client) {

        if (client.player == null || client.world == null) return;

        KeyBindings.handle(client);

        if (ENABLED) {
            AutoMossHandler.tick(client);
        }
    }
}