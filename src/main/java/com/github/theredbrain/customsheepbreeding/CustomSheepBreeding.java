package com.github.theredbrain.customsheepbreeding;

import com.github.theredbrain.customsheepbreeding.config.ServerConfig;
import com.github.theredbrain.customsheepbreeding.config.ServerConfigWrapper;
import com.google.gson.Gson;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomSheepBreeding implements ModInitializer {
	public static final String MOD_ID = "customsheepbreeding";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ServerConfig serverConfig;

	private static PacketByteBuf configSerialized = PacketByteBufs.create();

	@Override
	public void onInitialize() {
		LOGGER.info("Sheep breeding was customized");

		// Config
		AutoConfig.register(ServerConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
		serverConfig = ((ServerConfigWrapper)AutoConfig.getConfigHolder(ServerConfigWrapper.class).getConfig()).server;

		configSerialized = ConfigSync.write(serverConfig);
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			sender.sendPacket(ConfigSync.ID, configSerialized);
		});
	}

	public static class ConfigSync {
		public static Identifier ID = new Identifier(MOD_ID, "config_sync");

		public static PacketByteBuf write(ServerConfig config) {
			var gson = new Gson();
			var json = gson.toJson(config);
			var buffer = PacketByteBufs.create();
			buffer.writeString(json);
			return buffer;
		}

		public static ServerConfig read(PacketByteBuf buffer) {
			var gson = new Gson();
			var json = buffer.readString();
			return gson.fromJson(json, ServerConfig.class);
		}
	}
}