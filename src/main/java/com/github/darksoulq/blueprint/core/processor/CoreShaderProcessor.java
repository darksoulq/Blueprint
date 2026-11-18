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

public class CoreShaderProcessor implements ContentProcessor {

    @Override
    public void process(String namespace, File parentDir, File dataFile, ResourcesHolder holder) {
        if (!dataFile.getName().endsWith(".yml")) return;
        String name = dataFile.getName().replace(".yml", "");

        byte[] vsh = holder.getAsset(namespace, "shaders/core", name + ".vsh");
        byte[] fsh = holder.getAsset(namespace, "shaders/core", name + ".fsh");
        byte[] jsonBytes = null;

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(dataFile), StandardCharsets.UTF_8)) {
            Yaml yamlParser = new Yaml();
            Object raw = yamlParser.load(reader);
            if (raw != null) {
                JsonObject json = YamlUtil.toJson(raw).getAsJsonObject();
                jsonBytes = json.toString().getBytes(StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            Blueprint.getPlugin().getLogger().warning("Failed to parse core shader YAML: " + dataFile.getName() + " - " + e.getMessage());
        }

        if (vsh != null || fsh != null || jsonBytes != null) {
            holder.getPack().namespace(namespace).coreShader(name, vsh, fsh, jsonBytes);
            BlueprintRegistries.CORE_SHADER_COUNT++;
        } else {
            Blueprint.getPlugin().getLogger().warning("Core shader '" + name + "' has no valid .yml config, .vsh, or .fsh files.");
        }
    }
}