package com.github.theredbrain.customsheepbreeding.config;

import com.github.theredbrain.customsheepbreeding.CustomSheepBreeding;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

@Config(
        name = CustomSheepBreeding.MOD_ID
)
public class ServerConfigWrapper extends PartitioningSerializer.GlobalData {
    @ConfigEntry.Category("server")
    @ConfigEntry.Gui.Excluded
    public ServerConfig server = new ServerConfig();
}
