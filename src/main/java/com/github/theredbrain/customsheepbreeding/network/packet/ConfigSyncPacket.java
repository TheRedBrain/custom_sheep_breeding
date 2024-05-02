package com.github.theredbrain.customsheepbreeding.network.packet;

import com.github.theredbrain.customsheepbreeding.CustomSheepBreeding;
import com.github.theredbrain.customsheepbreeding.config.ServerConfig;
import com.google.gson.Gson;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record ConfigSyncPacket(ServerConfig config) implements CustomPayload {
    public static final Id<ConfigSyncPacket> PACKET_ID = new Id<>(CustomSheepBreeding.identifier("config_sync"));
    public static final PacketCodec<RegistryByteBuf, ConfigSyncPacket> PACKET_CODEC = PacketCodec.of(ConfigSyncPacket::write, ConfigSyncPacket::new);

    public ConfigSyncPacket(RegistryByteBuf registryByteBuf) {
        this(new Gson().fromJson(registryByteBuf.readString(), ServerConfig.class));
    }

    public void write(RegistryByteBuf buf) {
        var gson = new Gson();
        var json = gson.toJson(config);
        buf.writeString(json);
    }
    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
