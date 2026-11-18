package com.github.darksoulq.blueprint.core.processor;

import com.github.darksoulq.abyssallib.common.config.Config;
import com.github.darksoulq.abyssallib.server.registry.DeferredRegistry;
import com.github.darksoulq.abyssallib.server.registry.Registries;
import com.github.darksoulq.abyssallib.world.entity.CustomEntity;
import com.github.darksoulq.blueprint.Blueprint;
import com.github.darksoulq.blueprint.core.BlueprintRegistries;
import com.github.darksoulq.blueprint.core.ResourcesHolder;
import com.github.darksoulq.blueprint.core.codec.EntityCodecs;
import com.github.darksoulq.blueprint.core.codec.EntityDef;
import com.github.darksoulq.blueprint.core.entity.ScriptedCustomEntity;
import com.github.darksoulq.blueprint.core.scripting.ScriptManager;
import com.github.darksoulq.blueprint.core.scripting.api.EntityBehavior;

import java.io.File;

public class EntityProcessor implements ContentProcessor {

    private final ScriptManager scriptManager;

    public EntityProcessor(ScriptManager scriptManager) {
        this.scriptManager = scriptManager;
    }

    @Override
    public void process(String namespace, File parentDir, File dataFile, ResourcesHolder holder) {
        if (!dataFile.getName().endsWith(".yml")) return;

        String nameNoExt = dataFile.getName().replace(".yml", "");
        Config config = new Config(parentDir, nameNoExt);
        Config.Value<EntityDef> val = config.value("", null, EntityCodecs.ENTITY_DEF);
        EntityDef def = val.get();
        if (def == null) {
            Blueprint.getPlugin().getLogger().warning("Failed to decode EntityDef for " + dataFile.getName());
            return;
        }

        File ktsFile = new File(parentDir, nameNoExt + ".kts");
        EntityBehavior behavior = null;
        if (ktsFile.exists()) {
            Blueprint.getPlugin().getLogger().info("Parsing entity behavior script: " + ktsFile.getName());
            Object result = scriptManager.eval(ktsFile);

            if (result instanceof EntityBehavior) {
                behavior = (EntityBehavior) result;
                Blueprint.getPlugin().getLogger().info("Successfully loaded entity behavior for: " + nameNoExt);
            } else if (result == null) {
                Blueprint.getPlugin().getLogger().severe("Entity behavior script " + ktsFile.getName() + " returned null! Check console for errors.");
            } else {
                Blueprint.getPlugin().getLogger().severe("Entity behavior script " + ktsFile.getName() + " returned invalid type: " + result.getClass().getName());
            }
        }

        EntityBehavior finalBehavior = behavior;

        DeferredRegistry<CustomEntity> registry = BlueprintRegistries.ENTITY_REGISTRIES.get(namespace);
        if (registry == null) {
            registry = DeferredRegistry.create(Registries.ENTITIES, namespace);
            BlueprintRegistries.ENTITY_REGISTRIES.register(namespace, registry);
        }

        registry.register(nameNoExt, key -> new ScriptedCustomEntity(key, def, finalBehavior));
        BlueprintRegistries.ENTITY_COUNT++;
    }
}