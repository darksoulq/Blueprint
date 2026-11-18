package com.github.darksoulq.blueprint.core.processor;

import com.github.darksoulq.blueprint.core.BlueprintRegistries;
import com.github.darksoulq.blueprint.core.ResourcesHolder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class AtlasProcessor implements ContentProcessor {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void process(String namespace, File parentDir, File dataFile, ResourcesHolder holder) {
        String atlasId = dataFile.getName().replace(".yml", "");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(dataFile);
        
        if (yaml.contains("sources") && yaml.isList("sources")) {
            List<Map<?, ?>> sources = yaml.getMapList("sources");
            
            String json = GSON.toJson(Map.of("sources", sources));
            holder.getPack().namespace(namespace).atlas(atlasId, json.getBytes(StandardCharsets.UTF_8));
            BlueprintRegistries.ATLAS_COUNT++;
        }
    }
}