package com.github.darksoulq.blueprint.core;

import com.github.darksoulq.abyssallib.server.resource.ResourcePack;
import com.github.darksoulq.blueprint.Blueprint;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ResourcesHolder {

    private final Blueprint plugin;
    private ResourcePack pack;

    public ResourcesHolder(Blueprint plugin) {
        this.plugin = plugin;
        this.pack = new ResourcePack(plugin, Blueprint.PLUGIN_ID);
    }

    public byte[] getAsset(String namespace, String type, String path) {
        File file = new File(plugin.getDataFolder(), "content/" + namespace + "/assets/" + type + "/" + path);
        if (!file.exists()) {
            return null;
        }
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            return null;
        }
    }

    public ResourcePack getPack() {
        return pack;
    }

    public void registerPack() {
        pack.register(true);
    }

    public void unregisterPack() {
        pack.unregister();
    }

    public void reloadPack() {
        this.pack = new ResourcePack(plugin, Blueprint.PLUGIN_ID);
    }
}