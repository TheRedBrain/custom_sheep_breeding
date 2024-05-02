package com.github.theredbrain.customsheepbreeding;

import com.github.theredbrain.customsheepbreeding.network.packet.ConfigSyncPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class CustomSheepBreedingClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {

		PayloadTypeRegistry.playS2C().register(ConfigSyncPacket.PACKET_ID, ConfigSyncPacket.PACKET_CODEC);
		ClientPlayNetworking.registerGlobalReceiver(ConfigSyncPacket.PACKET_ID, ((payload, context) -> {
			CustomSheepBreeding.serverConfig = payload.config();
		}));
	}
}