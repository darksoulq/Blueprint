package com.github.darksoulq.blueprint.core.processor;

import com.github.darksoulq.blueprint.Blueprint;
import com.github.darksoulq.blueprint.core.BlueprintRegistries;
import com.github.darksoulq.blueprint.core.ResourcesHolder;
import com.github.darksoulq.blueprint.core.util.YamlUtil;
import com.google.gson.JsonObject;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class PostEffectProcessor implements ContentProcessor {

    @Override
    public void process(String namespace, File parentDir, File dataFile, ResourcesHolder holder) {
        if (!dataFile.getName().endsWith(".yml")) return;
        String name = dataFile.getName().replace(".yml", "");

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(dataFile), StandardCharsets.UTF_8)) {
            Yaml yamlParser = new Yaml();
            Object raw = yamlParser.load(reader);
            if (raw != null) {
                JsonObject json = YamlUtil.toJson(raw).getAsJsonObject();
                byte[] jsonBytes = json.toString().getBytes(StandardCharsets.UTF_8);
                holder.getPack().namespace(namespace).postEffect(name, jsonBytes);
                BlueprintRegistries.POST_EFFECT_COUNT++;
            }
        } catch (Exception e) {
            Blueprint.getPlugin().getLogger().warning("Failed to parse post effect YAML: " + dataFile.getName() + " - " + e.getMessage());
        }
    }
}