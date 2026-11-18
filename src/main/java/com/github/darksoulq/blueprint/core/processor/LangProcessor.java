package com.github.darksoulq.blueprint.core.processor;

import com.github.darksoulq.abyssallib.server.resource.asset.Lang;
import com.github.darksoulq.blueprint.Blueprint;
import com.github.darksoulq.blueprint.core.BlueprintRegistries;
import com.github.darksoulq.blueprint.core.ResourcesHolder;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class LangProcessor implements ContentProcessor {

    @Override
    public void process(String namespace, File parentDir, File dataFile, ResourcesHolder holder) {
        String locale = dataFile.getName().replace(".yml", "").toLowerCase(java.util.Locale.ROOT);
        Lang langAsset = holder.getPack().namespace(namespace).lang(locale, false);
        boolean loadedAny = false;

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(dataFile), StandardCharsets.UTF_8)) {
            Yaml yamlParser = new Yaml();
            Object raw = yamlParser.load(reader);

            if (raw instanceof Map<?, ?> map) {
                loadedAny = flattenMap("", map, langAsset);
            } else if (raw instanceof List<?> list) {
                for (Object item : list) {
                    if (item instanceof Map<?, ?> map) {
                        if (flattenMap("", map, langAsset)) loadedAny = true;
                    }
                }
            }
        } catch (Exception e) {
            Blueprint.getPlugin().getLogger().warning("Failed to parse language file: " + dataFile.getName() + " - " + e.getMessage());
        }

        if (loadedAny) {
            BlueprintRegistries.LANG_COUNT++;
        }
    }

    private boolean flattenMap(String prefix, Map<?, ?> map, Lang langAsset) {
        boolean loadedAny = false;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String key = String.valueOf(entry.getKey());
            String fullKey = prefix.isEmpty() ? key : prefix + "." + key;

            if (entry.getValue() instanceof Map<?, ?> nested) {
                if (flattenMap(fullKey, nested, langAsset)) loadedAny = true;
            } else if (entry.getValue() != null) {
                langAsset.put(fullKey, String.valueOf(entry.getValue()));
                loadedAny = true;
            }
        }
        return loadedAny;
    }
}