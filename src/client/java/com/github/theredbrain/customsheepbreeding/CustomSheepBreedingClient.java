package com.github.theredbrain.customsheepbreeding;

import com.github.theredbrain.customsheepbreeding.network.packet.ConfigSyncPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class CustomSheepBreedingClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {

		ClientPlayNetworking.registerGlobalReceiver(ConfigSyncPacket.PACKET_ID, ((payload, context) -> {
			CustomSheepBreeding.serverConfig = payload.config();
		}));
	}
}