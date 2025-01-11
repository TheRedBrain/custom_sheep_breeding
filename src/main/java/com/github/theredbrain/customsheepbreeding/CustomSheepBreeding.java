package com.github.theredbrain.customsheepbreeding;

import com.github.theredbrain.customsheepbreeding.config.ServerConfig;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomSheepBreeding implements ModInitializer {
	public static final String MOD_ID = "customsheepbreeding";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ServerConfig SERVER_CONFIG;

	@Override
	public void onInitialize() {
		LOGGER.info("Sheep breeding was customized");
		SERVER_CONFIG = ConfigApiJava.registerAndLoadConfig(ServerConfig::new, RegisterType.BOTH);
	}

	public static Identifier identifier(String path) {
		return Identifier.of(MOD_ID, path);
	}
}