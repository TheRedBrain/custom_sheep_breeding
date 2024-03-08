package com.github.theredbrain.customsheepbreeding;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class CustomSheepBreedingClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {

		ClientPlayNetworking.registerGlobalReceiver(CustomSheepBreeding.ConfigSync.ID, (client, handler, buf, responseSender) -> {
			CustomSheepBreeding.serverConfig = CustomSheepBreeding.ConfigSync.read(buf);
		});
	}
}