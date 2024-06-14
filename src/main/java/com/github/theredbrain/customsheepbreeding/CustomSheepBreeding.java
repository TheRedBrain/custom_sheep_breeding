package com.github.theredbrain.customsheepbreeding;

import com.github.theredbrain.customsheepbreeding.config.ServerConfig;
import com.github.theredbrain.customsheepbreeding.config.ServerConfigWrapper;
import com.github.theredbrain.customsheepbreeding.network.packet.ConfigSyncPacket;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomSheepBreeding implements ModInitializer {
	public static final String MOD_ID = "customsheepbreeding";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ServerConfig serverConfig;

	@Override
	public void onInitialize() {
		LOGGER.info("Sheep breeding was customized");

		// Config
		AutoConfig.register(ServerConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
		serverConfig = ((ServerConfigWrapper)AutoConfig.getConfigHolder(ServerConfigWrapper.class).getConfig()).server;

		PayloadTypeRegistry.playS2C().register(ConfigSyncPacket.PACKET_ID, ConfigSyncPacket.PACKET_CODEC);
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			sender.sendPacket(new ConfigSyncPacket(serverConfig));
		});
	}

	public static Identifier identifier(String path) {
		return Identifier.of(MOD_ID, path);
	}
}