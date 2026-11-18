package com.github.darksoulq.blueprint.core.processor;

import com.github.darksoulq.abyssallib.common.config.Config;
import com.github.darksoulq.abyssallib.server.registry.DeferredRegistry;
import com.github.darksoulq.abyssallib.server.registry.Registries;
import com.github.darksoulq.abyssallib.world.item.Item;
import com.github.darksoulq.blueprint.Blueprint;
import com.github.darksoulq.blueprint.core.BlueprintRegistries;
import com.github.darksoulq.blueprint.core.ResourcesHolder;
import com.github.darksoulq.blueprint.core.codec.ItemCodecs;
import com.github.darksoulq.blueprint.core.codec.ItemDef;
import com.github.darksoulq.blueprint.core.item.ScriptedCustomItem;
import com.github.darksoulq.blueprint.core.scripting.ScriptManager;
import com.github.darksoulq.blueprint.core.scripting.api.ItemBehavior;

import java.io.File;

public class ItemProcessor implements ContentProcessor {

    private final ScriptManager scriptManager;

    public ItemProcessor(ScriptManager scriptManager) {
        this.scriptManager = scriptManager;
    }

    @Override
    public void process(String namespace, File parentDir, File dataFile, ResourcesHolder holder) {
        if (!dataFile.getName().endsWith(".yml")) return;

        String nameNoExt = dataFile.getName().replace(".yml", "");
        Config config = new Config(parentDir, nameNoExt);
        Config.Value<ItemDef> val = config.value("", null, ItemCodecs.ITEM_DEF);
        ItemDef def = val.get();
        if (def == null) {
            Blueprint.getPlugin().getLogger().warning("Failed to decode ItemDef for " + dataFile.getName());
            return;
        }

        File ktsFile = new File(parentDir, nameNoExt + ".kts");
        ItemBehavior behavior = null;
        if (ktsFile.exists()) {
            Blueprint.getPlugin().getLogger().info("Parsing item behavior script: " + ktsFile.getName());
            Object result = scriptManager.eval(ktsFile);

            if (result instanceof ItemBehavior) {
                behavior = (ItemBehavior) result;
                Blueprint.getPlugin().getLogger().info("Successfully loaded item behavior for: " + nameNoExt);
            } else if (result == null) {
                Blueprint.getPlugin().getLogger().severe("Item behavior script " + ktsFile.getName() + " returned null! Check console for errors.");
            } else {
                Blueprint.getPlugin().getLogger().severe("Item behavior script " + ktsFile.getName() + " returned invalid type: " + result.getClass().getName());
            }
        }

        ItemBehavior finalBehavior = behavior;

        DeferredRegistry<Item> registry = BlueprintRegistries.ITEM_REGISTRIES.get(namespace);
        if (registry == null) {
            registry = DeferredRegistry.create(Registries.ITEMS, namespace);
            BlueprintRegistries.ITEM_REGISTRIES.register(namespace, registry);
        }

        registry.register(nameNoExt, key -> new ScriptedCustomItem(key, def, finalBehavior));
        BlueprintRegistries.ITEM_COUNT++;
    }
}