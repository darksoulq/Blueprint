package com.github.darksoulq.blueprint.core;

import com.github.darksoulq.abyssallib.server.registry.DeferredRegistry;
import com.github.darksoulq.blueprint.Blueprint;
import com.github.darksoulq.blueprint.core.processor.AtlasProcessor;
import com.github.darksoulq.blueprint.core.processor.ContentProcessor;
import com.github.darksoulq.blueprint.core.processor.CoreShaderProcessor;
import com.github.darksoulq.blueprint.core.processor.EntityProcessor;
import com.github.darksoulq.blueprint.core.processor.GlyphProcessor;
import com.github.darksoulq.blueprint.core.processor.GoalProcessor;
import com.github.darksoulq.blueprint.core.processor.ItemProcessor;
import com.github.darksoulq.blueprint.core.processor.LangProcessor;
import com.github.darksoulq.blueprint.core.processor.ParticleProcessor;
import com.github.darksoulq.blueprint.core.processor.PostEffectProcessor;
import com.github.darksoulq.blueprint.core.processor.SoundProcessor;
import com.github.darksoulq.blueprint.core.scripting.ScriptManager;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class ContentLoader {

    private final Blueprint plugin;
    private final ResourcesHolder resourcesHolder;
    private final Map<String, ContentProcessor> processors = new LinkedHashMap<>();

    public ContentLoader(Blueprint plugin, ResourcesHolder resourcesHolder, ScriptManager scriptManager) {
        this.plugin = plugin;
        this.resourcesHolder = resourcesHolder;

        processors.put("sounds", new SoundProcessor());
        processors.put("glyphs", new GlyphProcessor());
        processors.put("lang", new LangProcessor());
        processors.put("atlases", new AtlasProcessor());
        processors.put("particles", new ParticleProcessor());
        processors.put("goals", new GoalProcessor(scriptManager));
        processors.put("entities", new EntityProcessor(scriptManager));
        processors.put("items", new ItemProcessor(scriptManager));
        processors.put("core_shaders", new CoreShaderProcessor());
        processors.put("post_effects", new PostEffectProcessor());
    }

    public void loadAll() {
        BlueprintRegistries.clearAll();
        File contentDir = new File(plugin.getDataFolder(), "content");

        if (!contentDir.exists()) {
            contentDir.mkdirs();
            return;
        }

        File[] namespaces = contentDir.listFiles();
        if (namespaces == null) return;

        ComponentLogger logger = plugin.getComponentLogger();

        for (File nsDir : namespaces) {
            if (!nsDir.isDirectory()) continue;
            String ns = nsDir.getName();

            logger.info(MiniMessage.miniMessage().deserialize("<aqua>Loading namespace:</aqua> <gold>" + ns + "</gold>"));

            File dataDir = new File(nsDir, "data");
            if (!dataDir.exists() || !dataDir.isDirectory()) continue;

            for (Map.Entry<String, ContentProcessor> entry : processors.entrySet()) {
                File typeDir = new File(dataDir, entry.getKey());
                if (!typeDir.exists() || !typeDir.isDirectory()) continue;

                File[] files = typeDir.listFiles();
                if (files == null) continue;

                for (File file : files) {
                    if (file.getName().endsWith(".yml") || file.getName().endsWith(".kts")) {
                        entry.getValue().preProcess(ns, typeDir, file, resourcesHolder);
                    }
                }
            }

            for (Map.Entry<String, ContentProcessor> entry : processors.entrySet()) {
                File typeDir = new File(dataDir, entry.getKey());
                if (!typeDir.exists() || !typeDir.isDirectory()) continue;

                File[] files = typeDir.listFiles();
                if (files == null) continue;

                for (File file : files) {
                    if (file.getName().endsWith(".yml") || file.getName().endsWith(".kts")) {
                        entry.getValue().process(ns, typeDir, file, resourcesHolder);
                    }
                }
            }
        }

        BlueprintRegistries.ENTITY_REGISTRIES.getAll().values().forEach(DeferredRegistry::apply);
        BlueprintRegistries.ITEM_REGISTRIES.getAll().values().forEach(DeferredRegistry::apply);

        logger.info(MiniMessage.miniMessage().deserialize(
            "<green>Successfully loaded <white>" + BlueprintRegistries.SOUND_COUNT +
                "</white> sounds, <white>" + BlueprintRegistries.GLYPH_COUNT +
                "</white> glyphs, <white>" + BlueprintRegistries.LANG_COUNT +
                "</white> languages, <white>" + BlueprintRegistries.ATLAS_COUNT +
                "</white> atlases, <white>" + BlueprintRegistries.PARTICLE_COUNT +
                "</white> particles, <white>" + BlueprintRegistries.GOAL_COUNT +
                "</white> goals, <white>" + BlueprintRegistries.ENTITY_COUNT +
                "</white> entities, <white>" + BlueprintRegistries.ITEM_COUNT +
                "</white> items, <white>" + BlueprintRegistries.CORE_SHADER_COUNT +
                "</white> core shaders, and <white>" + BlueprintRegistries.POST_EFFECT_COUNT +
                "</white> post effects.</green>"
        ));
    }
}