package com.github.darksoulq.blueprint.core.processor;

import com.github.darksoulq.abyssallib.common.config.Config;
import com.github.darksoulq.abyssallib.server.resource.asset.SoundEntry;
import com.github.darksoulq.abyssallib.server.resource.asset.SoundEvent;
import com.github.darksoulq.blueprint.Blueprint;
import com.github.darksoulq.blueprint.core.BlueprintRegistries;
import com.github.darksoulq.blueprint.core.ResourcesHolder;
import com.github.darksoulq.blueprint.core.codec.BlueprintCodecs;
import com.github.darksoulq.blueprint.core.codec.SoundDef;
import com.github.darksoulq.blueprint.core.codec.SoundVariantDef;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class SoundProcessor implements ContentProcessor {

    @Override
    public void process(String namespace, File parentDir, File dataFile, ResourcesHolder holder) {
        String nameNoExt = dataFile.getName().replace(".yml", "");
        Config config = new Config(parentDir, nameNoExt);
        YamlConfiguration keysConfig = YamlConfiguration.loadConfiguration(dataFile);

        for (String eventKey : keysConfig.getKeys(false)) {
            Config.Value<SoundDef> val = config.value(eventKey, null, BlueprintCodecs.SOUND_DEF);
            SoundDef def = val.get();
            if (def == null) {
                Blueprint.getPlugin().getLogger().warning("Failed to parse SoundDef for '" + eventKey + "' in " + dataFile.getName());
                continue;
            }

            SoundEvent event = holder.getPack().namespace(namespace).sounds().event(eventKey);
            event.category(SoundCategory.valueOf(def.category().toUpperCase()));
            if (def.subtitle() != null) event.subtitle(def.subtitle());
            event.replace(def.replace());

            for (SoundVariantDef variant : def.sounds()) {
                byte[] data = holder.getAsset(namespace, "sounds", variant.name() + ".ogg");
                if (data != null) {
                    SoundEntry entry = event.addVariant(variant.name(), data);
                    entry.volume(variant.volume())
                        .pitch(variant.pitch())
                        .weight(variant.weight())
                        .stream(variant.stream())
                        .attenuationDistance(variant.attenuationDistance())
                        .preload(variant.preload())
                        .type(variant.type());
                }
            }
            BlueprintRegistries.SOUND_COUNT++;
        }
    }
}