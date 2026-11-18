package com.github.darksoulq.blueprint.core.processor;

import com.github.darksoulq.abyssallib.common.config.Config;
import com.github.darksoulq.abyssallib.common.serialization.Codec;
import com.github.darksoulq.abyssallib.common.serialization.Codecs;
import com.github.darksoulq.abyssallib.world.particle.Generator;
import com.github.darksoulq.abyssallib.world.particle.ParticleRenderer;
import com.github.darksoulq.abyssallib.world.particle.Transformer;
import com.github.darksoulq.blueprint.Blueprint;
import com.github.darksoulq.blueprint.core.BlueprintRegistries;
import com.github.darksoulq.blueprint.core.ResourcesHolder;
import com.github.darksoulq.blueprint.core.codec.ParticleCodecs;
import com.github.darksoulq.blueprint.core.particle.ParticleTemplate;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ParticleProcessor implements ContentProcessor {

    @Override
    public void preProcess(String namespace, File parentDir, File dataFile, ResourcesHolder holder) {
        String nameNoExt = dataFile.getName().replace(".yml", "");
        Config config = new Config(parentDir, nameNoExt);
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(dataFile);

        if (yaml.contains("generators")) {
            Config.Value<Map<String, Generator>> val = config.value("generators", new HashMap<>(), Codec.map(Codecs.STRING, ParticleCodecs.GENERATOR));
            if (val.get() != null) {
                val.get().forEach((k, v) -> BlueprintRegistries.GENERATORS.register(namespace + ":" + k, v));
            } else {
                Blueprint.getPlugin().getLogger().warning("Failed to parse particle generators in " + dataFile.getName());
            }
        }

        if (yaml.contains("renderers")) {
            Config.Value<Map<String, ParticleRenderer>> val = config.value("renderers", new HashMap<>(), Codec.map(Codecs.STRING, ParticleCodecs.RENDERER));
            if (val.get() != null) {
                val.get().forEach((k, v) -> BlueprintRegistries.RENDERERS.register(namespace + ":" + k, v));
            } else {
                Blueprint.getPlugin().getLogger().warning("Failed to parse particle renderers in " + dataFile.getName());
            }
        }

        if (yaml.contains("transformers")) {
            Config.Value<Map<String, Transformer>> val = config.value("transformers", new HashMap<>(), Codec.map(Codecs.STRING, ParticleCodecs.TRANSFORMER));
            if (val.get() != null) {
                val.get().forEach((k, v) -> BlueprintRegistries.TRANSFORMERS.register(namespace + ":" + k, v));
            } else {
                Blueprint.getPlugin().getLogger().warning("Failed to parse particle transformers in " + dataFile.getName());
            }
        }
    }

    @Override
    public void process(String namespace, File parentDir, File dataFile, ResourcesHolder holder) {
        String nameNoExt = dataFile.getName().replace(".yml", "");
        Config config = new Config(parentDir, nameNoExt);
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(dataFile);

        if (yaml.contains("templates")) {
            Config.Value<Map<String, ParticleTemplate>> val = config.value("templates", new HashMap<>(), Codec.map(Codecs.STRING, ParticleCodecs.TEMPLATE));
            if (val.get() != null) {
                val.get().forEach((k, v) -> {
                    BlueprintRegistries.PARTICLE_TEMPLATES.register(namespace + ":" + k, v);
                    BlueprintRegistries.PARTICLE_COUNT++;
                });
            } else {
                Blueprint.getPlugin().getLogger().warning("Failed to parse particle templates in " + dataFile.getName());
            }
        }
    }
}